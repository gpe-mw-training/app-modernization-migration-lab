package com.redhat.coolstore.catalog.rest;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.MavenResolvedArtifact;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wildfly.swarm.Swarm;
import org.wildfly.swarm.arquillian.CreateSwarm;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.redhat.coolstore.catalog.model.Product;
import com.redhat.coolstore.catalog.service.CatalogService;
import org.keycloak.jose.jws.Algorithm;
import org.keycloak.jose.jws.JWSBuilder;
import org.keycloak.representations.AccessToken;
import org.keycloak.util.TokenUtil;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Security;
import java.security.spec.PKCS8EncodedKeySpec;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import java.util.Optional;
import org.jboss.shrinkwrap.resolver.api.maven.ScopeType;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;

@RunWith(Arquillian.class)
public class RestApiTest {

	private static String port = System.getProperty("arquillian.swarm.http.port", "18080");

	private Client client;

	@CreateSwarm
	public static Swarm newContainer() throws Exception {
		Properties properties = new Properties();
		properties.put("swarm.http.port", port);
		return new Swarm(properties).withProfile("local");
	}

	@Deployment
	public static Archive<?> createDeployment() {
	
        MavenResolvedArtifact[] artifacts = Maven.resolver().loadPomFromFile("pom.xml")
                .importDependencies(ScopeType.TEST).resolve()
                .withoutTransitivity().asResolvedArtifact();
        Optional<MavenResolvedArtifact> dep = Arrays.stream(artifacts)
                .filter(a -> a.getCoordinate().getArtifactId().equals("bcprov-jdk15on")).findFirst();

        WebArchive archive =  ShrinkWrap.create(WebArchive.class, "catalog-service.war")
                .addPackages(true,CatalogResource.class.getPackage())
				.addPackage(CatalogService.class.getPackage())
				.addPackage(Product.class.getPackage())
                .addAsResource("project-local.yml", "project-local.yml")
                .addAsResource("META-INF/test-persistence.xml",  "META-INF/persistence.xml")
                .addAsResource("META-INF/test-load.sql",  "META-INF/test-load.sql")
                .addAsWebInfResource("keycloak.json","keycloak.json");

        dep.ifPresent(d -> archive.addAsLibrary(d.asFile()));
        return archive;
	}

	@Before
	public void before() throws Exception {
		client = ClientBuilder.newClient();
	}

	@After
	public void after() throws Exception {
		client.close();
	}

	@Test
	@RunAsClient
	public void testGetProduct() throws Exception {
		WebTarget target = client.target("http://localhost:" + port).path("/catalog/product").path("/123456");
		//Response response = target.request(MediaType.APPLICATION_JSON).get();
		Response response = target.request(MediaType.APPLICATION_JSON)
			    .header("Authorization", "Bearer " + getValidAccessToken("coolstore")).get();

		assertThat(response.getStatus(), equalTo(new Integer(200)));
		JsonObject value = Json.parse(response.readEntity(String.class)).asObject();
		assertThat(value.getString("itemId", null), equalTo("123456"));
		assertThat(value.getString("name", null), equalTo("catalog"));
		assertThat(value.getString("description",null), equalTo("CatalogServiceTest"));
		assertThat(value.getDouble("price", 0.0), equalTo(new Double(34)));
	}

	@Test
	@RunAsClient
	public void testGetProductWhenItemIdDoesNotExist() throws Exception {
		WebTarget target = client.target("http://localhost:" + port).path("/catalog/product").path("/doesnotexist");
		Response response = target.request(MediaType.APPLICATION_JSON).get();
		assertThat(response.getStatus(), equalTo(new Integer(404)));
	}

	@Test
	@RunAsClient
	public void testHealthCheckCombined() throws Exception {
		WebTarget target = client.target("http://localhost:" + port).path("/health");
		Response response = target.request(MediaType.APPLICATION_JSON).get();
		assertThat(response.getStatus(), equalTo(new Integer(200)));
		JsonObject value = Json.parse(response.readEntity(String.class)).asObject();
		assertThat(value.getString("outcome", ""), equalTo("UP"));
		JsonArray checks = value.get("checks").asArray();
		assertThat(checks.size(), equalTo(new Integer(1)));
		JsonObject state = checks.get(0).asObject();
		assertThat(state.getString("id", ""), equalTo("server-state"));
		assertThat(state.getString("result", ""), equalTo("UP"));
	}

	@Test
	@RunAsClient
	public void testHealthCheckStatus() throws Exception {
		WebTarget target = client.target("http://localhost:"+ port).path("/status");
		Response response = target.request(MediaType.APPLICATION_JSON).get();
		assertThat(response.getStatus(), equalTo(new Integer(200)));
		JsonObject value = Json.parse(response.readEntity(String.class)).asObject();
		assertThat(value.getString("id", ""), equalTo("server-state"));
		assertThat(value.getString("result", ""), equalTo("UP"));
	}

	private PrivateKey readPrivateKey() throws Exception {
		Security.addProvider(new BouncyCastleProvider());
		KeyFactory factory = KeyFactory.getInstance("RSA", "BC");
		InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("private.pem");
		PemReader privateKeyReader = new PemReader(new InputStreamReader(is));
		try {
			PemObject privObject = privateKeyReader.readPemObject();
			PKCS8EncodedKeySpec privKeySpec = new PKCS8EncodedKeySpec(privObject.getContent());
			PrivateKey privateKey = factory.generatePrivate(privKeySpec);
			return privateKey;
		} finally {
			privateKeyReader.close();
		}
	}

		private String createAccessToken(String role, int issuedAt) throws Exception {
			AccessToken token = new AccessToken();
			token.type(TokenUtil.TOKEN_TYPE_BEARER);
			token.subject("testuser");
			token.issuedAt(issuedAt);
			token.issuer("https://secure-sso-rhsso.127.0.0.1.nip.io/auth/realms/coolstore");
			token.expiration(issuedAt + 300);
			token.setAllowedOrigins(new HashSet<>());

			AccessToken.Access access = new AccessToken.Access();
			token.setRealmAccess(access);
			access.addRole(role);

			Algorithm jwsAlgorithm = Algorithm.RS256;
			PrivateKey privateKey = readPrivateKey();
			String encodedToken = new JWSBuilder().type("JWT").jsonContent(token).sign(jwsAlgorithm, privateKey);
			return encodedToken;
		}

		private String getValidAccessToken(String role) throws Exception {
			return createAccessToken(role, (int) (System.currentTimeMillis() / 1000));
		}

		private String getExpiredAccessToken(String role) throws Exception {
			return createAccessToken(role, (int) ((System.currentTimeMillis() / 1000)-600));
		}

}
