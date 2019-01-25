set repodir="[Local repo location]"
set desdir="[Local destination location]"
cd %repodir%
for /f %%i in ('git describe --tags') do set gittag=%%i
IF NOT EXIST %desdir% (
	mkdir %desdir%
)
copy %repodir%\target\NabAlexa-0.0.1-SNAPSHOT.jar %desdir%\NabAlexa_%gittag%.jar
pause