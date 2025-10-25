import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import SockJS from 'sockjs-client';
import { Client, IMessage } from '@stomp/stompjs';
import { ApiService } from '../service/api.service';
import { CommonModule, NgIf } from '@angular/common';

@Component({
  selector: 'app-payment',
  imports: [CommonModule, NgIf],
  templateUrl: './payment.component.html',
  styleUrl: './payment.component.css'
})
export class PaymentComponent implements OnInit, OnDestroy {
  isProcessing = true;
  status = 'PENDING';
  initialCode: any;
  orderId: any = null;

  // Use the modern Client from @stomp/stompjs
  private stompClient: Client | null = null;

  constructor(private route: ActivatedRoute) {
    console.log('💥 PaymentComponent CONSTRUCTOR called!');
    console.log('Current URL:', window.location.href);

    // Test ngay trong constructor
    alert('PaymentComponent loaded!');
  }

  ngOnInit() {
    console.log('🚀 PaymentComponent ngOnInit() - Component loaded!');

    // (1) Lấy orderId từ URL (do Backend redirect về)
    this.orderId = this.route.snapshot.queryParamMap.get('orderId');
    this.initialCode = this.route.snapshot.queryParamMap.get('responseCode');

    console.log('📦 Query params:', { orderId: this.orderId, responseCode: this.initialCode });

    // (2) Kết nối WebSocket
    this.connectWebSocket();
  }

  connectWebSocket() {
    console.log('🔌 Bắt đầu kết nối WebSocket...');

    const socket = new SockJS('http://localhost:8080/ws-payment'); // Endpoint WebSocket

    console.log('📡 SockJS socket created');

    // Create a new STOMP Client and provide a webSocketFactory that returns the SockJS socket
    this.stompClient = new Client({
      webSocketFactory: () => socket as any,
      // Optional: auto-reconnect after X ms
      reconnectDelay: 5000,
      debug: (str) => {
        console.log('STOMP Debug:', str); //
      },

    });

    this.stompClient.onConnect = (frame) => {
      console.log('✅ Connected to WebSocket: ', frame);


      this.stompClient!.subscribe(`/topic/payment-status/${this.orderId}`, (message: IMessage) => {

        // (4) Nhận được tin nhắn từ Backend!
        const statusDto = JSON.parse(message.body);

        console.log('📨 Payment status update received!', statusDto);

        // Cập nhật UI
        this.status = statusDto.status;
        this.isProcessing = false;

        this.disconnectWebSocket(); // Xử lý xong, ngắt kết nối
      });

      console.log(`📻 Subscribed to topic: /topic/payment-status/${this.orderId}`);
    };

    this.stompClient.onStompError = (frame) => {
      console.error('❌ STOMP Error:', frame);
    };

    this.stompClient.onWebSocketError = (event) => {
      console.error('❌ WebSocket Error:', event);
    };

    // Activate the client to open the connection
    console.log('⚡ Activating STOMP client...');
    this.stompClient.activate();
  }

  ngOnDestroy() {
    this.disconnectWebSocket();
  }

  disconnectWebSocket() {
    if (this.stompClient) {
      // Use deactivate() for the modern @stomp/stompjs Client
      this.stompClient.deactivate();
      this.stompClient = null;
    }
  }
}
