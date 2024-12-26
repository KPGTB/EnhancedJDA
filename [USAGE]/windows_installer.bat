@ECHO off


echo  ^_^_^_^_^_           ^_           ^_   ^_^_^_^_^_^_       ^_                              ^_
echo ^|  ^_^_ \         (^_)         ^| ^| ^|  ^_^_^_^_^|     ^| ^|                            ^| ^|
echo ^| ^|^_^_) ^| ^_^_ ^_^_^_  ^_  ^_^_^_  ^_^_^_^| ^|^_^| ^|^_^_   ^_ ^_^_ ^| ^|^_^_   ^_^_ ^_ ^_ ^_^_   ^_^_^_ ^_^_^_  ^_^_^| ^|
echo ^|  ^_^_^_/ '^_^_/ ^_ \^| ^|/ ^_ \/ ^_^_^| ^_^_^|  ^_^_^| ^| '^_ \^| '^_ \ / ^_` ^| '^_ \ / ^_^_/ ^_ \/ ^_` ^|
echo ^| ^|   ^| ^| ^| (^_) ^| ^|  ^_^_/ (^_^_^| ^|^_^| ^|^_^_^_^_^| ^| ^| ^| ^| ^| ^| (^_^| ^| ^| ^| ^| (^_^|  ^_^_/ (^_^| ^|
echo ^|^_^|   ^|^_^|  \^_^_^_/^| ^|\^_^_^_^|\^_^_^_^|\^_^_^|^_^_^_^_^_^_^|^_^| ^|^_^|^_^| ^|^_^|\^_^_,^_^|^_^| ^|^_^|\^_^_^_\^_^_^_^|\^_^_,^_^|
echo                ^_/ ^|
echo               ^|^_^_/
echo.
echo Welcome in discord bot executable generator
echo Windows MSI edition
echo.
echo ===========================================
echo.
echo Information about application
echo.
echo ===========================================
echo.
set /p "name=Enter name of application: "
set /p "version=Enter version of application: "
set /p "description=Enter description of application: "
set /p "copyright=Enter copyright of application: "
set /p "vendor=Enter vendor: "
set /p "help=Enter help URL: "
set /p "about=Enter about URL: "
echo.
echo ===========================================
echo.
echo Paths
echo.
echo ===========================================
echo.
set /p "input=Enter directory with input JAR: "
set /p "output=Enter directory with output MSI: "
set /p "jar=Enter full name of JAR file (with .jar): "
set /p "main=Enter full name of main class (with package): "
set /p "icon=Enter path to icon (.ico): "
set /p "license=Enter path to file with license: "
echo.
echo ===========================================
echo.
echo Build
echo.
echo ===========================================
echo.
set /p "additional=Enter additional jpackage args: "
echo.
echo Building installer...

jpackage --name "%name%" --app-version "%version%" --description "%description%" --copyright "%copyright%" --vendor "%vendor%" --win-help-url "%help%" --about-url "%about%" --input "%input%" --dest "%output%" --main-jar "%jar%" --main-class "%main%" --icon "%icon%" --license-file "%license%" --type msi  --win-console --win-dir-chooser --win-shortcut-prompt --win-shortcut --win-menu %additional%

echo Done!
pause
