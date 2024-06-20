import {Injectable} from '@angular/core';
import {
  Connection,
  PublicKey,
  SystemProgram,
  TransactionMessage,
  VersionedTransaction
} from '@solana/web3.js';
import {AuthService} from './auth.service';
import {UserService} from "./user.service";
import {OrderDetailDto, OrderListDto} from "../dtos/order";
import {catchError, from, map, Observable, switchMap} from "rxjs";
import {tap} from "rxjs/operators";
import placeholder from "lodash/fp/placeholder";

declare global {
  interface Window {
    solana?: any;
  }
}

@Injectable({
  providedIn: 'root'
})
export class WalletService {

  private connection: Connection;
  private companyWallet: PublicKey;

  constructor(private userService: UserService, private authService: AuthService) {
    this.connection = new Connection('https://api.devnet.solana.com');
    this.companyWallet = new PublicKey('8acQJLKzFLvD4KxtZmnhMcfZreYx7H4A2gd8Z4kT5C6w');
  }

  /**
   * Checks if the wallet is present.
   */
  isWalletPresent(): boolean {
    return 'solana' in window;
  }

  /**
   * Connects the wallet and returns the public key.
   */
  async connectWallet(): Promise<string> {
    const provider = window.solana as SolanaProvider;
    if (provider) {
      await provider.connect();
      return provider.publicKey.toString();
    }
    throw new Error('Solana provider not found');
  }

  /**
   * Disconnects the wallet and logs out the user.
   */
  async disconnectWallet() {
    try {
      const provider = window.solana as SolanaProvider;
      if (provider) {
        await provider.disconnect();
        this.authService.logoutUser();
      }
    } catch (error) {
      console.error('Error disconnecting wallet:', error);
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
      const solBalance = balance / 1e9;
      return parseFloat(solBalance.toFixed(4));
    } catch (error) {
      console.error('Error getting wallet balance:', error);
      throw error;
    }
  }

  /**
   * Checks if the wallet has sufficient balance.
   *
   * @param amount The amount to check
   */
  async hasSufficientBalance(amount: number): Promise<boolean> {
    try {
      const provider = window.solana as SolanaProvider;
      const publicKey = provider.publicKey.toString();
      const balance = await this.getBalance(publicKey);
      return balance >= amount;
    } catch (error) {
      console.error('Error checking balance', error);
      return false;
    }
  }


  /**
   * Creates a transfer transaction.
   *
   * @param publicKey The public key of the wallet
   * @param connection The connection to the network
   * @param recipientPublicKey The public key of the recipient
   * @param amount The amount to transfer
   */
  createTransferTransactionV0 = async (
    publicKey: PublicKey,
    connection: Connection,
    recipientPublicKey: PublicKey,
    amount: number
  ): Promise<VersionedTransaction> => {
    const {blockhash} = await connection.getLatestBlockhash();
    const instructions = [
      SystemProgram.transfer({
        fromPubkey: publicKey,
        toPubkey: recipientPublicKey,
        lamports: amount * 1e9, // lamports to SOL
      }),
    ];

    const message = new TransactionMessage({
      payerKey: publicKey,
      recentBlockhash: blockhash,
      instructions,
    }).compileToV0Message();

    return new VersionedTransaction(message);
  };

  /**
   * Handles the transaction by signing and sending it.
   *
   * @param amount The amount to transfer
   */
  handleSignAndSendTransaction(amount: number): Observable<OrderDetailDto> {
    if (!this.authService.isLoggedIn()) {
      console.error('User not logged in');
      return;
    }
    const provider = window.solana as SolanaProvider;
    if (!provider) {
      console.error('No solana provider found');
      return;
    }
    let txSignature = '';
    return from(this.createTransferTransactionV0(provider.publicKey, this.connection, this.companyWallet, amount))
      .pipe(
        switchMap(transactionV0 => this.signAndSendTransaction(provider, transactionV0)),
        switchMap(signature => {
          txSignature = signature;
          return this.pollSignatureStatus(signature, this.connection)
        }),
        switchMap(confirmed => {
          if (confirmed) {
            let orderCreateDto = {signature: txSignature};
            return this.userService.verifyAndCreateOrder(orderCreateDto)
          } else {
            console.error('Transaction failed');
            throw new Error('Transaction failed');
          }
        })
      );
  }

  /**
   * Polls the signature status of the transaction.
   *
   * @param signature The signature of the transaction
   * @param connection The connection to the network
   */
  async pollSignatureStatus(signature: string, connection: Connection): Promise<boolean> {
    let count = 0;
    const MAX_POLLS = 30;

    while (count < MAX_POLLS) {
      const {value} = await connection.getSignatureStatus(signature);
      const confirmationStatus = value?.confirmationStatus;
      if (confirmationStatus) {
        const hasReachedSufficientCommitment = confirmationStatus === 'confirmed' || confirmationStatus === 'finalized';
        if (hasReachedSufficientCommitment) {
          return true;
        }
      }
      count++;
      await new Promise(resolve => setTimeout(resolve, 1000));
    }
    console.error(`Failed to confirm transaction within ${MAX_POLLS} seconds. The transaction may or may not have succeeded.`);
    return false;
  }

  /**
   * Signs and sends the transaction.
   *
   * @param provider The Solana provider
   * @param transactionv0 The transaction to sign and send
   */
  async signAndSendTransaction(provider: SolanaProvider, transactionv0: VersionedTransaction): Promise<string> {
    try {
      const {signature} = await provider.signAndSendTransaction(transactionv0);
      return signature;
    } catch (error) {
      console.warn(error);
      throw new Error(error.message);
    }
  }
}

interface SolanaProvider {
  publicKey: PublicKey;
  connect: () => Promise<void>;
  disconnect: () => Promise<void>;
  signAndSendTransaction: (transaction: VersionedTransaction) => Promise<{ signature: string }>;
}
