#  Grbl Config
    Grbl 1.1f ['$' for help]
    $0=5
    $1=255
    $2=0
    $3=0
    $4=1
    $5=0
    $6=0
    $10=1
    $11=0.010
    $12=0.002
    $13=0
    $20=0
    $21=0
    $22=0
    $23=0
    $24=32.000
    $25=32.000
    $26=32
    $27=1.000
    $30=1000
    $31=1000
    $32=0
    $100=32.000
    $101=32.000
    $102=32.000
    $110=60000.000
    $111=60000.000
    $112=60000.000
    $120=10000.000
    $121=2000.000
    $122=2000.000
    $130=200.000
    $131=200.000
    $132=200.000

# Pin Config
D2 Step X
D3 Step Y
D4 Step Z

D5 Dir X
D6 Dir Y
D7 Dir Z

D13 (DIR Spindle) Focus
D11 (PWM Spindle) Trigger

# Links
* Arduino Nano 
    * Schematic: https://www.arduino.cc/en/uploads/Main/ArduinoNano30Schematic.pdf

* Arduino Nano Clone witch CH340
    * Description: http://actrl.cz/blog/index.php/2016/arduino-nano-ch340-schematics-and-details/
    * Schematic: http://actrl.cz/blog/wp-content/uploads/nano_ch340_schematics.pdf

* CH340 (Arduino Nano clone USB->Serial) 
    * Datasheet: https://cdn.sparkfun.com/datasheets/Dev/Arduino/Other/CH340DS1.PDF
    * Drivers: http://sparks.gogo.co.nz/ch340.html

* Grbl (Motion control Software for CNC milling)
    * Code: https://github.com/gnea/grbl
    * Wiki: https://github.com/gnea/grbl/wiki
    * Flash Arduinos with Grbl: https://github.com/gnea/grbl/wiki/Compiling-Grbl

* HC-05 (Bluetooth <-> Serial)
    * Datasheet: http://cdn.makezine.com/uploads/2014/03/hc_hc-05-user-instructions-bluetooth.pdf
     
* DRV8825 (Stepper Driver)
    * Datasheet: http://www.ti.com/lit/ds/symlink/drv8825.pdf
    * RepRap Module description: http://reprap.org/wiki/MKS_DRV8825

* Stepper Motor i took 17HS19-1684S-PG19
    * Has a gear ratio of 19 + 38/187 ~ 19.2
    * Datasheet: https://www.omc-stepperonline.com/download/17HS19-1684S-PG19.pdf
    * From here: https://www.omc-stepperonline.com/geared-stepper-motor/nema-17-stepper-motor-bipolar-l48mm-w-gear-raio-191-planetary-gearbox-17hs19-1684s-pg19.html
     
* Possible better Steppers:
    * https://www.omc-stepperonline.com/geared-stepper-motor/nema-17-stepper-motor-l39mm-gear-raio-201-high-precision-planetary-gearbox-17hs15-1684s-hg20.html
    * http://www.stepperonline.info/geared-stepper-motor/nema-17-stepper-motor-l39mm-gear-raio-101-high-precision-planetary-gearbox-17hs15-1684s-hg10.html?mfp=65-gear-backlash-max%5B0.25%5D