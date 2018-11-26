FROM fedora:26

# Installing httpd 
RUN echo "Installing httpd"; dnf -y install httpd

#Create default index.html
RUN  echo "Congratulations, the container is created using Dockerfile"  > /var/www/html/index.html

# Expose the default httpd port 80
EXPOSE 80

# Run the httpd
CMD ["/usr/sbin/httpd", "-DFOREGROUND"]
