@echo off 
set CarpetaOrigen=D:\TAS-ON\PROYECTOS\TAS-ON-WEB\tas_on_web\
set CarpetaDestino=D:\TAS-ON\jboss-eap-7.0\standalone\deployments\

copy %CarpetaOrigen%tas-on-web-services\target\*.war %CarpetaDestino%
copy %CarpetaOrigen%tas-on-web-jobs\tas-on-web-job-expiration\target\*.war %CarpetaDestino%
copy %CarpetaOrigen%tas-on-web-facturacion\target\*.war %CarpetaDestino%

