import { Injectable } from '@angular/core';
import {Connection, PublicKey} from "@solana/web3.js";
import {AuthService} from "./auth.service";

@Injectable({
  providedIn: 'root'
})
export class WalletService {

  private connection: Connection;

  constructor(
    private authService: AuthService,
  ) {
    // custom solana rpc, since mainnet gets CORS error
    this.connection = new Connection('https://mainnet.helius-rpc.com/?api-key=050b86ea-d180-480a-88b5-f4bf5211eb57');
  }

  /**
   * Connects the wallet and returns the public key.
   */
  async connectWallet(): Promise<string> {
    await window.solana.connect();
    return window.solana.publicKey.toString();
  }

  /**
   * Disconnects the wallet and logs out the user.
   */
  async disconnectWallet() {
    try {
      if ("solana" in window) {
        await window.solana.disconnect();
        this.authService.logoutUser();
      }
    } catch (error) {
      console.error("Error disconnecting wallet:", error);
    }
  }

  /**
   * Fetches the balance of the wallet.
   *
   * @param publicKey The public key of the wallet
   */
  async getBalance(publicKey: string): Promise<number> {
    try {
      const balance = await this.connection.getBalance(new PublicKey(publicKey.toString()));
      // balance in LAMPORTS, divide by 1e9 to get SOL
      const solBalance =  balance / 1e9;
      return parseFloat(solBalance.toFixed(4));
    } catch (error) {
      console.error('Error getting wallet balance:', error);
      throw error;
    }
  }
}

