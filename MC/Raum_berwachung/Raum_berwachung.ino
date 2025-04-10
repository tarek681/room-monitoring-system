#include <lmic.h>
#include <hal/hal.h>
#include <SPI.h>
#include <LowPower.h>
#include <Wire.h> 
#include  "adcvcc.h"  
#include  "BME280I2C.h"  //bis zur Version 2.1.4


// global enviromental parameters

static unsigned char mydata[7]={0};      
volatile static unsigned char Bewegung=0;

const int wakeUpPin = 2;
const int wakeUpPin1 = 3;
void wakeUp();
void wakeUp();

byte LMIC_transmitted = 0;
byte LMIC_event_Timeout = 0;

volatile int statuse=0;
volatile int Fenster=0;

static osjob_t sendjob;

BME280I2C bme;                   // Default : forced mode, standby time = 1000 ms
// Oversampling = pressure ×1, temperature ×1, humidity ×1, filter off,


/* Defines*/ 

/////Eingansdaten

#define Fenster_P  2
#define Licht_P    A0

#define debugSerial Serial 
#define SHOW_DEBUGINFO  
#define debugPrintLn(...) { if (debugSerial) debugSerial.println(__VA_ARGS__); }
#define debugPrint(...) { if (debugSerial) debugSerial.print(__VA_ARGS__); } 

#define INTERVAL    300   // every 5 Min.


// Pin mapping CH2I (check out : https://www.thethingsnetwork.org/forum/t/full-arduino-mini-lorawan-and-1-3ua-sleep-mode/8059 ) 
#define LMIC_NSS    6
#define LMIC_RXTX   LMIC_UNUSED_PIN
#define LMIC_RST    5
#define LMIC_DIO0   9
#define LMIC_DIO1   8
#define LMIC_DIO2   7


const lmic_pinmap lmic_pins = {
    .nss = LMIC_NSS,
    .rxtx = LMIC_RXTX,   
    .rst = LMIC_RST,
    .dio = {LMIC_DIO0, LMIC_DIO1, LMIC_DIO2},  
}; 

// UPDATE WITH YOURE TTN KEYS AND ADDR
static const PROGMEM u1_t NWKSKEY[16] ={ 0xBA, 0x66, 0x69, 0x68, 0xCD, 0x88, 0x26, 0x37, 0xE7, 0x8A, 0x21, 0x70, 0x3E, 0x73, 0x97, 0xA4 }; // LoRaWAN NwkSKey, network session key (msb)
static const u1_t PROGMEM APPSKEY[16] = { 0x67, 0xB1, 0xEB, 0x5B, 0x50, 0x4F, 0xF2, 0x21, 0x26, 0x5C, 0x66, 0x91, 0xB2, 0x43, 0xB2, 0x3D }; // LoRaWAN AppSKey, application session key (msb)
static const u4_t DEVADDR = 0x260111C4; // LoRaWAN end-device address (DevAddr)

// These callbacks are only used in over-the-air activation, so they are
// left empty here (we cannot leave them out completely unless
// DISABLE_JOIN is set in config.h, otherwise the linker will complain).
void os_getArtEui (u1_t* buf) { }
void os_getDevEui (u1_t* buf) { }
void os_getDevKey (u1_t* buf) { }

ISR(ADC_vect)  
{
  // Increment ADC counter
  _adc_irq_cnt++;
}

void updateEnvParameters()
{
    delay(1000); 

        
        
     // Daten aufrufen und dem datenfeld zuweisen
    mydata[0]=(int)(readVcc() / 100);
    mydata[1]=(int)(bme.hum()-20) & 0xFF;
    mydata[2]= ((int)((bme.temp(true) + 40.0) * 10.0)) >> 8;  //  Temperature: -40…85°C 
    mydata[3] = ((int)((bme.temp(true) + 40.0) * 10.0)) & 0xFF;
    mydata[4] = (int)(bme.pres(1)) >> 8;                     //  Pressure: 300...1100 hPa
    mydata[5] = (int)(bme.pres(1)) & 0xFF; 
    mydata[6] = Bewegung;
    mydata[6] =  mydata[6]+((digitalRead(Fenster_P))<<1);
    mydata[6] =  mydata[6]+(((analogRead(Licht_P)/200)&&1)<<2);
    
} 

void onEvent (ev_t ev) 
{
    debugPrint(os_getTime()); 
    debugPrint(": ");
    debugPrintLn(ev);
    

    switch(ev) 
    {
        case EV_SCAN_TIMEOUT:
            //debugPrintLn(F("EV_SCAN_TIMEOUT"));
            break;
        case EV_BEACON_FOUND:
            //debugPrintLn(F("EV_BEACON_FOUND"));
            break;
        case EV_BEACON_MISSED:
            //debugPrintLn(F("EV_BEACON_MISSED"));
            break;
        case EV_BEACON_TRACKED:
            //debugPrintLn(F("EV_BEACON_TRACKED"));
            break;
        case EV_JOINING:
            //debugPrintLn(F("EV_JOINING"));
            break;
        case EV_JOINED:
            //debugPrintLn(F("EV_JOINED"));
            break;
        case EV_RFU1:
            //debugPrintLn(F("EV_RFU1"));
            break;
        case EV_JOIN_FAILED:
            //debugPrintLn(F("EV_JOIN_FAILED"));
            break;
        case EV_REJOIN_FAILED:
            //debugPrintLn(F("EV_REJOIN_FAILED"));
            break;
        case EV_TXCOMPLETE:
            debugPrintLn(F("EV_TXCOMPLETE"));
            if (LMIC.txrxFlags & TXRX_ACK)
              debugPrintLn(F("R ACK")); // Received ack
            if (LMIC.dataLen) 
            {
              debugPrintLn(F("R "));
              debugPrintLn(LMIC.dataLen);
              debugPrintLn(F(" bytes")); // of payload
            }            
            // Schedule next transmission
            // os_setTimedCallback(&sendjob, os_getTime()+sec2osticks(TX_INTERVAL), do_send);
            LMIC_transmitted = 1; 
            break;
        case EV_LOST_TSYNC:
            //debugPrintLn(F("EV_LOST_TSYNC"));
            break;
        case EV_RESET:
            //debugPrintLn(F("EV_RESET"));
            break;
        case EV_RXCOMPLETE:
            // data received in ping slot
            //debugPrintLn(F("EV_RXCOMPLETE"));
            break;
        case EV_LINK_DEAD:
            //debugPrintLn(F("EV_LINK_DEAD"));
            break;
        case EV_LINK_ALIVE:
            //debugPrintLn(F("EV_LINK_ALIVE"));
            break;
         default:
            //debugPrintLn(F("Unknown event"));
            break;
    }
}

void do_send(osjob_t* j)
{ 
    
    
 
    // Check if there is not a current TX/RX job running
    if (LMIC.opmode & OP_TXRXPEND) 
    {
        debugPrintLn(F("OP_TXRXPEND")); //P_TXRXPEND, not sending
    } 
    else 
    {
        // Prepare upstream data transmission at the next possible time.
       
         updateEnvParameters();
     
        LMIC_setTxData2(1, mydata, sizeof(mydata), 0);
         
       // debugPrintLn(F("PQ")); //Packet queued
        
    }
   
}



void sende(){
    // Start job
      
    do_send(&sendjob);
     
    // Wait for response of the queued message (check if message is send correctly)
    os_runloop_once();
     
    // Continue until message is transmitted correctly
    //debugPrintLn("\tWaiting for transmittion\n"); 
  //  debugPrintLn(F("W\n"));
    while(LMIC_transmitted != 1) 
    {
        os_runloop_once();
        // Add timeout counter when nothing happens:
        LMIC_event_Timeout++;
        delay(1000);
        if (LMIC_event_Timeout >= 60) 
        {
            // Timeout when there's no "EV_TXCOMPLETE" event after 60 seconds
            debugPrintLn(F("\tETimeout, msg not tx\n"));
            break;
        } 
    }

    LMIC_transmitted = 0;
    LMIC_event_Timeout = 0;
   

  return;
  }




void wakeUp()
{
 
statuse=1;

    return ;
}

void wakeUp1()
{
 
statuse=1;
Bewegung =1;
    return ;
}

void setup() {

     //pin 9 als Eingang für die Bewegungsdaten
  // pinMode(Bewg_P,INPUT);
    //pin 8 als Eingang für die Fensterstatusdaten
   pinMode(Fenster_P,INPUT);
    //pin A0 als Eingang für die Lichtintensitätsdaten
   pinMode(Licht_P,INPUT);

  pinMode(wakeUpPin,INPUT);
    Serial.begin(9600);
    debugPrintLn(F("Starting"));
    
    if (!bme.begin()) 
    {  
        debugPrintLn(F("No valid bme280 sensor!"));
        delay(1000);  // allow serial to send.
        while (1) 
        {
           LowPower.powerDown(SLEEP_8S, ADC_OFF, BOD_OFF);   
        }
    } 
  
    // LMIC init
    os_init();
    // Reset the MAC state. Session and pending data transfers will be discarded.
    LMIC_reset();

    // Set static session parameters. Instead of dynamically establishing a session
    // by joining the network, precomputed session parameters are be provided.
    #ifdef PROGMEM
    // On AVR, these values are stored in flash and only copied to RAM
    // once. Copy them to a temporary buffer here, LMIC_setSession will
    // copy them into a buffer of its own again.
    uint8_t appskey[sizeof(APPSKEY)];
    uint8_t nwkskey[sizeof(NWKSKEY)];
    memcpy_P(appskey, APPSKEY, sizeof(APPSKEY));
    memcpy_P(nwkskey, NWKSKEY, sizeof(NWKSKEY));
    LMIC_setSession (0x1, DEVADDR, nwkskey, appskey);
    #else
    // If not running an AVR with PROGMEM, just use the arrays directly
    LMIC_setSession (0x1, DEVADDR, NWKSKEY, APPSKEY);
    #endif

    #if defined(CFG_eu868)
    // Set up the channels used by the Things Network, which corresponds
    // to the defaults of most gateways. Without this, only three base
    // channels from the LoRaWAN specification are used, which certainly
    // works, so it is good for debugging, but can overload those
    // frequencies, so be sure to configure the full frequency range of
    // your network here (unless your network autoconfigures them).
    // Setting up channels should happen after LMIC_setSession, as that
    // configures the minimal channel set.
    // NA-US channels 0-71 are configured automatically
    LMIC_setupChannel(0, 868100000, DR_RANGE_MAP(DR_SF12, DR_SF7),  BAND_CENTI);      // g-band
    LMIC_setupChannel(1, 868300000, DR_RANGE_MAP(DR_SF12, DR_SF7B), BAND_CENTI);      // g-band
    LMIC_setupChannel(2, 868500000, DR_RANGE_MAP(DR_SF12, DR_SF7),  BAND_CENTI);      // g-band
    LMIC_setupChannel(3, 867100000, DR_RANGE_MAP(DR_SF12, DR_SF7),  BAND_CENTI);      // g-band
    LMIC_setupChannel(4, 867300000, DR_RANGE_MAP(DR_SF12, DR_SF7),  BAND_CENTI);      // g-band
    LMIC_setupChannel(5, 867500000, DR_RANGE_MAP(DR_SF12, DR_SF7),  BAND_CENTI);      // g-band
    LMIC_setupChannel(6, 867700000, DR_RANGE_MAP(DR_SF12, DR_SF7),  BAND_CENTI);      // g-band
    LMIC_setupChannel(7, 867900000, DR_RANGE_MAP(DR_SF12, DR_SF7),  BAND_CENTI);      // g-band
    LMIC_setupChannel(8, 868800000, DR_RANGE_MAP(DR_FSK,  DR_FSK),  BAND_MILLI);      // g2-band


    // For single channel gateways: Restrict to channel 0 when defined above
#ifdef CHANNEL0
    LMIC_disableChannel(1);
    LMIC_disableChannel(2);
    LMIC_disableChannel(3);
    LMIC_disableChannel(4);
    LMIC_disableChannel(5);
    LMIC_disableChannel(6);
    LMIC_disableChannel(7);
    LMIC_disableChannel(8);
#endif

    // TTN defines an additional channel at 869.525Mhz using SF9 for class B
    // devices' ping slots. LMIC does not have an easy way to define set this
    // frequency and support for class B is spotty and untested, so this
    // frequency is not configured here.
    #elif defined(CFG_us915)
    // NA-US channels 0-71 are configured automatically
    // but only one group of 8 should (a subband) should be active
    // TTN recommends the second sub band, 1 in a zero based count.
    // https://github.com/TheThingsNetwork/gateway-conf/blob/master/US-global_conf.json
    LMIC_selectSubBand(1);
    #endif

    // Disable link check validation
    LMIC_setLinkCheckMode(0);

    // TTN uses SF9 for its RX2 window.
    LMIC.dn2Dr = DR_SF9;

    // Set data rate and transmit power for uplink (note: txpow seems to be ignored by the library)
    LMIC_setDrTxpow(DR_SF7,14); 
 

   // Allow wake up pins to trigger interrupt.
    attachInterrupt(0, wakeUp, RISING);
    attachInterrupt(1, wakeUp1, RISING);
    attachInterrupt(1, wakeUp1, FALLING);
}


void loop() 
{ 
 
 sende();
 
 Bewegung=0;
  
    for (int i = 0; i < INTERVAL; i+=8)
    {  
               
          // Enter power down state for 8 s with ADC and BOD module disabled
          LowPower.powerDown(SLEEP_8S, ADC_OFF, BOD_OFF);   
          if(statuse==1){
            sende();
            statuse=0;
                }
           

    }  
   

  
}
