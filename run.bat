@echo off
rem Windows: compile and run.   run.bat        -> demo
rem                             run.bat menu   -> interactive menu
cd /d "%~dp0"
if exist out rmdir /s /q out
mkdir out
dir /s /b src\*.java > sources.txt
javac -d out @sources.txt
del sources.txt
java -cp out library.Main %*
