@echo off

@echo "Change Directory: "
cd /d "E:\Anand Learning\textextractor\textscrapper\src\main\java\com\personal\textscrapper\util"

cd

echo Compiling Java files...
javac KeyBoardSimulator.java

if %errorlevel% neq 0 (
    echo Compilation failed!
    pause
    exit /b
)

echo Running Java program...


cd /d "E:\Anand Learning\textextractor\textscrapper\src\main\java"

cd

java com.personal.textscrapper.util.KeyBoardSimulator


pause