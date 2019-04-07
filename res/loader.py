import os
import zipfile

os.system('./gradlew distZip')

with zipfile.ZipFile("build/distributions/overlay.zip", "r") as zip_file:
    zip_file.extractall("build/tmp/")

os.system('./build/tmp/overlay/bin/overlay 0')
