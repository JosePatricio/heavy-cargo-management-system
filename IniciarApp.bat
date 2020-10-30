@echo off 

set ServidorActiveMq=D:\TAS-ON\apache-activemq-5.15.9\bin\activemq.bat
set ServidorJboss=D:\TAS-ON\jboss-eap-7.0\bin\standalone.bat
set AplicacionAngular=D:\TAS-ON\PROYECTOS\TAS-ON-WEB\tas_on_web\tas-on-web-app

start cmd /k %ServidorActiveMq% start
start cmd /k %ServidorJboss% -b 0.0.0.0

ping 127.0.0.1 -n 40 > nul

echo Iniciando App ...
start cmd /k "cd /d %AplicacionAngular% && node --max_old_space_size=4096 node_modules/@angular/cli/bin/ng build --bh /tas-on/ --prod"