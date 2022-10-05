// Name: Jiaqi Liu
// User ID: 904090572
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <arpa/inet.h>
#include <netinet/in.h>
#include <iostream>
#include <string>
using namespace std;
  
#define PORT     8080
#define MAX_PACKET_SIZE 512
#define MAX_PACKET_DATA_SIZE 502
void gremlin(char packet[], int length, double probability);
void reassemble(char packet[], char *fContent, int length);
bool errorDetection(char packet[], int length);

/* struct header {
    unsigned short checksum; //6 bytes
    char sequenceNum; //4 byte
};

struct packet {
    struct header headerData; // 10 bytes
    char data[MAX_PACKET_DATA_SIZE]; // 502 bytes
}; */

// Driver code
int main(int argc, char *argv[]) {
    int sockfd;
    char buffer[1024];
    char *hostname;
    char sendFile[1024];
    char recvFile[1024];
    double probability;
    int sendNum;
    int recNum;
    int fileLen;
    char *fileContent;
    if(argc != 2) {
        perror("error");
        exit(1);
    }
    hostname = argv[1];
    
    struct sockaddr_in     servaddr;
    
    // Creating socket file descriptor
    if ( (sockfd = socket(AF_INET, SOCK_DGRAM, 0)) < 0 ) {
        perror("socket creation failed");
        exit(EXIT_FAILURE);
    }
  
    memset(&servaddr, 0, sizeof(servaddr));
      
    // Filling server information
    servaddr.sin_family = AF_INET;
    servaddr.sin_port = htons(PORT);
    servaddr.sin_addr.s_addr = inet_addr(hostname);
    socklen_t len = sizeof(servaddr);
    int n;
    printf("***************************** UDP_Client is running **************************");
    // input the probability
    while(true) {
        cout << "Input the probability from 0 to 1: ";
        cin >> probability;
        if(probability < 0 || probability > 1) {
            cout << "Input again the probability: " << endl;
        }
        else {
            break;
        }
    }
    //input the url
    printf("Input the url you want: ");
    char url[MAX_PACKET_DATA_SIZE];
    cin >> url;
    string s_url = "GET ";
    s_url += url;
    s_url += " HTTP/1.0";
    strcpy(sendFile,s_url.c_str());
    // send the file name to the server 
    sendNum = sendto(sockfd, sendFile, strlen(sendFile), 0, (const struct sockaddr *) &servaddr,sizeof(servaddr));
    if(sendNum < 0) {
        perror("error");
        exit(1);
    }
    printf("Get request message sent. %s\n", sendFile);
    //receive the respond from server
    n = recvfrom(sockfd, (char *)buffer, sizeof(buffer), 0, (struct sockaddr *)&servaddr,&len);
    if(n < 0) {
        perror("error");
        exit(1);
    }
    printf("received the data:%s", buffer);
    FILE *fp = fopen(url, "w");
    if(fp == NULL) {
        perror("error");
        exit(1);
    }
    //receive the length from server
    n = recvfrom(sockfd, (char *)buffer, sizeof(buffer), 0, (struct sockaddr *)&servaddr,&len);
    if(n < 0) {
        perror("error");
        exit(1);
    }
    sscanf(buffer, "%d", &fileLen);
    printf(" the file length is : %d", fileLen);

    // initial the space of file content
    fileContent = (char *)malloc(sizeof(char));
    while(true) {//receive the data from server 
        recNum = recvfrom(sockfd, (char *)buffer, sizeof(buffer), 0, (struct sockaddr *)&servaddr,&len);
        if(recNum == 0) {
            cout << "null\n";
            break;
        }
        if(recNum < 0) {
            perror("error");
            exit(1);
        }
        
        // current packet
        int packetSize = recNum;
        char packet[packetSize];
        memset(&packet,0,sizeof(packet));

        for(int i = 0; i < packetSize; i++) {
            packet[i] = buffer[i];
        }
        
        // implement the gremlin method 
        gremlin(packet,packetSize,probability);
        // using packet[i] - '0' to convert string to int
        int sequenceNum = (packet[6] - '0') * 1000 + (packet[7] - '0') * 100 + (packet[8] - '0') * 10 + (packet[9] - '0');
        // detection the error
        if(errorDetection(packet,packetSize)) {
            cout << "no error" << endl;
        }
        else {
            cout << "error" << endl;
        }
        reassemble(packet,fileContent,packetSize);
    }
    fwrite(fileContent,sizeof(char),fileLen,fp);
    fclose(fp);
    free(fileContent);
    printf("The file :%s has been accepted.",url);
    printf("************************* UDP_Client exit ******************************");
    close(sockfd);
    return 0;
}
int calculateChecksum(char packet[], int length)
 {
     
     int checkSum = 0;
     
     for (int count = 6; count < length; count++)
         checkSum += (int)packet[count];
     return checkSum;
 }

void gremlin(char packet[], int length, double probability) {
    if(1.0 * rand() / RAND_MAX > probability) {
        return;
    }
    double n = 1.0 * rand() / RAND_MAX;
    if(n >= 0 && n < 0.5) {
        
        packet[(int)(1.0 * rand() / RAND_MAX * length)] = '0';
    }
    else if(n >= 0.5 && n < 0.8) {
        for(int i = 0; i < 2; i++) {
            
            packet[(int)(1.0 * rand() / RAND_MAX * length)] = '0';
        }
    }
    else {
        for(int j = 0; j < 3; j++) {
            
            packet[(int)(1.0 * rand() / RAND_MAX * length)] = '0';
        }
    }   
}

void reassemble(char packet[], char *fContent, int length) {
    int s = (packet[6] - '0') * 1000 + (packet[7] - '0') * 100 
        + (packet[8] - '0') * 10 + (packet[9] - '0');
    for(int i = 0; i < length - (MAX_PACKET_SIZE-MAX_PACKET_DATA_SIZE); i++) {
        fContent[s * MAX_PACKET_DATA_SIZE + i] = packet[i + (MAX_PACKET_SIZE-MAX_PACKET_DATA_SIZE)];
    }
}

bool errorDetection(char packet[], int length) {
    int sum = (packet[0] - '0') * 100000 + (packet[1] - '0') * 10000 + (packet[2] - '0') * 1000 
            + (packet[3] - '0') * 100 + (packet[4] - '0') * 10 + (packet[5] - '0');
    return sum == calculateChecksum(packet, length);
}


