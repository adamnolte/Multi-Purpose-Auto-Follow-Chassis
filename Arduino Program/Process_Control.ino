#include <SoftwareSerial.h>

int bTX = 2;
int bRX = 3;
int rightLED = 12;
int leftLED = 13;
int rightRec, leftRec;
bool autoFollow = false;
bool moved = false;
unsigned long lastMoved = 0;
bool stopped = true;
char incomingByte = '0';

int rightQ1 = 6;
int rightQ2 = 7;
int rightQ3 = 5;
int rightQ4 = 4;
int leftQ1 = 10;
int leftQ2 = 8;
int leftQ3 = 9;
int leftQ4 = 11;

SoftwareSerial bluetooth(bTX, bRX);

void setup() {
  Serial.begin(9600);
  //print for setup
  Serial.print("SETUP");
  bluetooth.begin(115200);
  delay(100);
  bluetooth.begin(9600);

  pinMode(leftQ1, OUTPUT);     //setting pin 4 for output
  pinMode(leftQ2, OUTPUT);     //setting pin 5 for output
  pinMode(leftQ3, OUTPUT);    //seting pin 6 for output
  pinMode(leftQ4, OUTPUT);    //setting pin 7 for output
  pinMode(rightQ1, OUTPUT);   //setting pin 8 for output
  pinMode(rightQ2, OUTPUT);   //setting pin 9 for output
  pinMode(rightQ3, OUTPUT);    //setting pin 10 for output
  pinMode(rightQ4, OUTPUT);     //setting pin 11 for output

  pinMode(rightLED, OUTPUT);
  pinMode(leftLED, OUTPUT);

  stopRobot();
  
}

void loop() {
    moved = false;
    
    if( bluetooth.available() > 0 ){
      incomingByte = (char)bluetooth.read();

      if(incomingByte == '4'){      //if user selects autofollow on android application
        autoFollow = true;
      }
      else if(incomingByte == '5'){   //if user selects manual control on android application
        autoFollow = false;
      }

      if(!autoFollow) {
        if (incomingByte == '0'){
          goForward();
          stopped = false;
          lastMoved = millis();
        }
        else if(incomingByte == '1'){
          goRight();
          lastMoved = millis();
          stopped = false;
        }
        else if(incomingByte == '2'){
          goBackward();
          stopped = false;
          lastMoved = millis();
        }
        else if(incomingByte == '3'){
          goLeft();
          stopped = false;
          lastMoved = millis();
        }
      }
    }

    if(autoFollow) {
      rightRec = analogRead(1);
      leftRec = analogRead(0);

      Serial.print("Right: ");
      Serial.print(rightRec);
      Serial.print(" Left: ");
      Serial.println(leftRec);

      if(rightRec > 200 && leftRec > 200) {
        if(rightRec < 600 && leftRec < 600) {
          goForward();
          Serial.println("Going Forward");
          stopped = false;
          lastMoved = millis();
        }

        else if(rightRec > 700 && leftRec < 500) {
          goRight();
          Serial.println("Going Right");
          stopped = false;
          lastMoved = millis();
        }

        else if(leftRec > 700 && rightRec < 500) {
          goLeft();
          Serial.println("Going Left");
          stopped = false;
          lastMoved = millis();
        }
      }

      else if(!stopped) {
        stopped = true;
        stopRobot();
      }
    }

    if(!autoFollow && !stopped && (millis() - lastMoved) > 300) {
      stopped = true;
      stopRobot();
    }

    if(autoFollow && !stopped && (millis() - lastMoved) > 800) {
      stopped = true;
      stopRobot();
    }
}

void goBackward(){

  digitalWrite(leftQ1, LOW);
  digitalWrite(leftQ2, HIGH);
  digitalWrite(leftQ3, LOW);
  digitalWrite(leftQ4, HIGH);

  digitalWrite(rightQ1, LOW);
  digitalWrite(rightQ2, HIGH);
  digitalWrite(rightQ3, LOW);
  digitalWrite(rightQ4, HIGH);
}

void goForward(){

  digitalWrite(rightLED, HIGH);
  digitalWrite(leftLED, HIGH);

  digitalWrite(leftQ1, HIGH);
  digitalWrite(leftQ2, LOW);
  digitalWrite(leftQ3, HIGH);
  digitalWrite(leftQ4, LOW);

  digitalWrite(rightQ1, HIGH);
  digitalWrite(rightQ2, LOW);
  digitalWrite(rightQ3, HIGH);
  digitalWrite(rightQ4, LOW);
}

void goLeft(){

  digitalWrite(rightLED, LOW);
  digitalWrite(leftLED, HIGH);
  
  digitalWrite(leftQ1, HIGH);
  digitalWrite(leftQ2, LOW);
  analogWrite(leftQ3, 140);
  digitalWrite(leftQ4, LOW);

  digitalWrite(rightQ1, HIGH);
  digitalWrite(rightQ2, LOW);
  digitalWrite(rightQ3, HIGH);
  digitalWrite(rightQ4, LOW);
}

void goRight(){

  digitalWrite(rightLED, HIGH);
  digitalWrite(leftLED, LOW);
  
  digitalWrite(leftQ1, HIGH);
  digitalWrite(leftQ2, LOW);
  digitalWrite(leftQ3, HIGH);
  digitalWrite(leftQ4, LOW);

  digitalWrite(rightQ1, HIGH);
  digitalWrite(rightQ2, LOW);
  analogWrite(rightQ3, 140);
  digitalWrite(rightQ4, LOW);
}

void stopRobot() {

  //Turn off LEDs
  digitalWrite(rightLED, LOW);
  digitalWrite(leftLED, LOW);
  
  digitalWrite(leftQ1, HIGH);
  digitalWrite(leftQ2, HIGH);
  digitalWrite(leftQ3, LOW);
  digitalWrite(leftQ4, LOW);

  digitalWrite(rightQ1, HIGH);
  digitalWrite(rightQ2, HIGH);
  digitalWrite(rightQ3, LOW);
  digitalWrite(rightQ4, LOW);
}

