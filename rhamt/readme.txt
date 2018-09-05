using httpd:2.4 to render reports. 
Based upon rhamt 4.1.0.Final
use command oc process -f ./httpd.json -pSOURCE_REPOSITORY_URL=http://gogs.gogs.svc.cluster.local:3000/gpte/rhamt.git -pSOURCE_REPOSITORY_REF=master  | oc create -f -
