@echo off
setlocal enabledelayedexpansion

REM Lista moduli da compilare
set modules=^
AuthenticationModule ^
CommonModule ^
HardwareManagerModule ^
HMIModule ^
MainBusinessLogicDpiModule ^
MainBusinessLogicKeyModule ^
MobileModule ^
SafetyModule ^
ZCarFleetModule

REM Base path moduli
set baseModulesPath=C:\Users\termo\Desktop\workspace\TruckingConfiguration\Modules

REM Compilazione moduli
for %%m in (%modules%) do (
    echo Compilo modulo: %%m
    cd /d "%baseModulesPath%\%%m"
    mvn clean install -Dmaven.test.skip=true -U
)



