set /p UserInputPath= File name
copy "%UserInputPath%" "./Decoder/production/simplesensorproject/%UserInputPath%"
cd Decoder/production/simplesensorproject
java -cp .;jdom2-2.0.3.jar rcas.stevenshighschool.apphysics2.projectcodename.simplesensorproject.Main %UserInputPath%
if "x%UserInputPath:~-4%"=="x.txt" (
    set UserInputPath=%UserInputPath:~0,-4%
)
mkdir "../../../OUTPUTS/%UserInputPath%"
copy "data.csv" "../../../OUTPUTS/%UserInputPath%/data.csv"
copy "data.kml" "../../../OUTPUTS/%UserInputPath%/data.kml"
cd ../../../OUTPUTS/%UserInputPath%/
copy "data.csv" "../../Data Vis/data.csv"
copy "data.csv" "../../Animations/data.csv"
cd ../../Data Vis
python "viscode.py"
copy "accelerations.html" "../OUTPUTS/%UserInputPath%/accelerations.html"
copy "accelerationtime.png" "../OUTPUTS/%UserInputPath%/accelerationtime.png"
copy "gravity.html" "../OUTPUTS/%UserInputPath%/gravity.html"
copy "gravitytime.png" "../OUTPUTS/%UserInputPath%/gravitytime.png"
copy "magnetic.html" "../OUTPUTS/%UserInputPath%/magnetic.html"
copy "magnetictime.png" "../OUTPUTS/%UserInputPath%/magnetictime.png"
copy "pressure.html" "../OUTPUTS/%UserInputPath%/pressure.html"
copy "pressuretime.png" "../OUTPUTS/%UserInputPath%/pressuretime.png"
copy "rotation.html" "../OUTPUTS/%UserInputPath%/rotation.html"
copy "rotationtime.png" "../OUTPUTS/%UserInputPath%/rotationtime.png"
cd ../Animations
@echo "OPEN FIREFOX AT localhost:8000, go to main.html, and cntrl-c to end in the cmd"
python -m http.server
cd ..