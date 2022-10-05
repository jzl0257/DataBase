//Name: Jiaqi Liu
//User ID: 904090572

#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <arpa/inet.h>
#include <netinet/in.h>
#include <netdb.h>
#include <string>
#include <iostream>

using namespace std;
  
#define PORT     8080
#define MAX_PACKET_SIZE 512
#define MAX_PACKET_DATA_SIZE 502

int calculateChecksum(char packet[], int length);

using std::string;

struct packet* segmentData(char *mes);

struct header {
    unsigned short checksum; //6 bytes
    char sequenceNum; //4 bytes
};

struct packet {
    struct header headerData; // 10 bytes
    char data[MAX_PACKET_DATA_SIZE]; // 502 bytes
};
  
// Driver code
int main() {
    int sockfd;
    char buffer[MAX_PACKET_SIZE];
    struct sockaddr_in servaddr, cliaddr;
    char fileContent[1024*1000];
    long numBytes;
    int packetNum = 1000;
    int contentLength;
    char sendBuff[1024];
    int sendNum;
    char *fContent;
    FILE *fp;
      
    // Creating socket file descriptor
    if ( (sockfd = socket(AF_INET, SOCK_DGRAM, 0)) < 0 ) {
        perror("socket creation failed");
        exit(EXIT_FAILURE);
    }
    // reset the address of server and client
    memset(&servaddr, 0, sizeof(servaddr));
    memset(&cliaddr, 0, sizeof(cliaddr));
      
    // Filling server information
    servaddr.sin_family    = AF_INET; // IPv4
    servaddr.sin_addr.s_addr = htonl(INADDR_ANY);
    servaddr.sin_port = htons(PORT);
      
    // Bind the socket with the server address
    if ( bind(sockfd, (const struct sockaddr *)&servaddr, sizeof(servaddr)) < 0 ){
        perror("bind failed");
        exit(1);
    }
      
    int n;
    socklen_t len;
  
    len = sizeof(cliaddr);  //len is value/resuslt

    printf("*****************************UDP_Server is runing******************************");
    // receive request from client 
    
    memset(&buffer,0,sizeof(buffer));
    n = recvfrom(sockfd, (char *)buffer, sizeof(buffer), MSG_WAITALL, ( struct sockaddr *) &cliaddr,&len);
    if(n < 0) {
        perror("error");
        exit(1);
    }
    printf("filename is: %s\n",buffer);
    printf("the size of buffer is :%lu\n",strlen(buffer));
    char fileName[256];
    // scan the file name from request 
    sscanf(buffer,"GET %s HTTP/1.0", fileName);
    // open the file
    fp = fopen(fileName,"r");
    if(fp == NULL) {
        perror("Open file failed\n");
        exit(1);
    }
    // get the number of file bytes
    fseek(fp, 0, SEEK_END);
    numBytes = ftell(fp);
    // set a space to store the data 
    fContent = (char *) malloc(sizeof(char) *numBytes);
    if(fContent == NULL) {
        perror("error");
        exit(1);
    }
    // set pointer to the initial char
    fseek(fp,0,SEEK_SET);
    
    contentLength = fread(fContent,sizeof(char),numBytes,fp);
    fclose(fp);
    // initial a space to store the send buffer
    memset(&sendBuff,0,sizeof(sendBuff));
    char conLen[contentLength];
    sprintf(conLen, "%ld", numBytes);
    string res = "HTTP/1.0 200 Document Follows\r\n";
    res += "Content-Type: text/plain\r\n";
    res += "Content-Length ";
    res += conLen;
    res += "\r\n\r\n";
    strcpy(sendBuff,res.c_str());
    
    // send the header to the client 
    sendNum = sendto(sockfd, sendBuff, strlen(sendBuff), 0, (const struct sockaddr *) &cliaddr,len);
    if(sendNum < 0) {
        perror("no header send");
        exit(1);
    }
    printf("The header data sent.\n"); 
    // send the file length
    string conL = conLen;
    strcpy(sendBuff,conL.c_str());
    sendNum = sendNum = sendto(sockfd, sendBuff, strlen(sendBuff), 0, (const struct sockaddr *) &cliaddr,len);
    if(sendNum < 0) {
        perror("no header send");
        exit(1);
    }

    int numOfPackets = (numBytes + (MAX_PACKET_DATA_SIZE -1)) / MAX_PACKET_DATA_SIZE;
    for(int i = 0; i < numOfPackets; i++) {
        // initialize the space of send buffer
        memset(sendBuff,0,sizeof(sendBuff));
        // check if the current packet is the final packet
        bool final = false;

        if(i >= 10000) {
            perror("error");
            exit(1);
        }

        int pSize = MAX_PACKET_SIZE;
        int headSize = MAX_PACKET_SIZE - MAX_PACKET_DATA_SIZE;
        if(i == numBytes/MAX_PACKET_DATA_SIZE){
            pSize = numBytes % MAX_PACKET_DATA_SIZE + headSize;
            final = true;
        }
        // initial a packet, and store the sequence num
        char packet[pSize];
        memset(packet,0,sizeof(packet));
        packet[6] = i / 1000 % 10 + '0';
        packet[7] = i / 100 %10 + '0';
        packet[8] = i / 10 %10 + '0';
        packet[9] = i % 10 + '0';
        // store the data to packet
        for(int k = headSize; k < pSize; k++) {
            packet[k] = fContent[i*MAX_PACKET_DATA_SIZE + (k - headSize)];
        }

        int checkSum = calculateChecksum(packet,pSize);
        packet[0] = checkSum / 100000 % 10 + '0';
        packet[1] = checkSum / 10000 % 10 + '0';
        packet[2] = checkSum / 1000 % 10 + '0';
        packet[3] = checkSum / 100 % 10 + '0';
        packet[4] = checkSum / 10 % 10 + '0';
        packet[5] = checkSum % 10 + '0';
        // store the packet to send buffer
        for(int j = 0; j < pSize; j++) {
            sendBuff[j] = packet[j];
        }
        printf("Send the packet:%d, packet size is: %lu\n",i,sizeof(packet));
        
        sendNum = sendto(sockfd, sendBuff, strlen(sendBuff), 0, (const struct sockaddr *) &cliaddr,len);
        if(sendNum < 0) {
            perror("error");
            exit(1);
        }
        if(final) {
            memset(&sendBuff,0,sizeof(sendBuff));
            sendNum = sendto(sockfd, sendBuff, 0, 0, (const struct sockaddr *) &cliaddr,len);
            if(sendNum < 0) {
                perror("error");
                exit(1);
            }
        }
    }

    free(fContent); 
    printf("*************************** The UDP_Server exit ****************************");
        
    return 0;
}
int calculateChecksum(char packet[], int length)
 {
     
     int checkSum = 0;
     
     for (int count = 6; count < length; count++)
         checkSum += (int)packet[count];
     return checkSum;
 }
