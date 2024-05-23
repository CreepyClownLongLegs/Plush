import {Component, Input, OnInit, TemplateRef, ViewChild} from "@angular/core";
import {AuthService} from "../../services/auth.service";
import {AuthRequest} from "../../dtos/auth-request";
import {NonceRequest} from "../../dtos/nonce-request";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {ToastrService} from "ngx-toastr";
import {WalletService} from "../../services/wallet.service";

export enum ButtonType {
  ConnectButton,
  CartButton,
}

@Component({
  selector: "app-login",
  templateUrl: "./login.component.html",
  styleUrls: ["./login.component.scss"],
})


export class LoginComponent implements OnInit {
  @ViewChild("walletModal") walletModal: TemplateRef<any>;
  @ViewChild("connectModal") connectModal: TemplateRef<any>;
  @Input() buttonClass: string;
  @Input() type: ButtonType;

  iconClass: string = '';
  label: string = '';
  error = false;
  errorMessage = "";
  publicKey = "";
  balance = 0;
  isDropdownOpen = false;

  constructor(
    public authService: AuthService,
    public walletService: WalletService,
    private modalService: NgbModal,
    private notification: ToastrService,
  ) {
  }

  ngOnInit() {
    if (this.type === ButtonType.ConnectButton) {
    }
    if (this.type === ButtonType.CartButton) {
      this.iconClass = "bag-icon";
    }
    if (this.authService.isLoggedIn()) {
      this.walletService.connectWallet().then(async (publicKey: string) => {
        this.publicKey = publicKey;
        this.balance = await this.walletService.getBalance(publicKey);
      })
        .catch((error) => {
          this.resetWalletConnection();
          console.error("Error connecting to wallet:", error);
        });
    }
  }

  buttonLabel() {
    if (this.type == ButtonType.ConnectButton) {
      if (this.authService.isLoggedIn()) {
        return this.publicKey ? this.formatWalletAddress(this.publicKey) : 'Connecting...'

      }
      return "Connect Wallet";
    }
    return '';
  }

  forwardToPhantom() {
    window.open('https://phantom.app/', '_blank');
    this.modalService.dismissAll();

  }

  handleButtonClick() {
    if (this.authService.isLoggedIn()) {
      this.openWalletModal();
    } else {
      this.openConnectModal();
    }
  }


  /**
   * Connects the wallet and tries to log in the user.
   */
  loginUser() {
    this.walletService.connectWallet().then(async (publicKey: string) => {
      this.publicKey = publicKey;
      this.getNonce(publicKey);
      this.balance = await this.walletService.getBalance(publicKey);
      this.modalService.dismissAll();

    })
      .catch((error) => {
        this.resetWalletConnection();
        console.error("Error connecting to wallet:", error);
        this.notification.error("Error connecting wallet", "Could Not Connect Wallet");
      });
  }

  /**
   * Fetches a nonce from the backend to sign it with the wallet.
   *
   * @param publicKey The pubKey of the wallet
   */
  getNonce(publicKey: string) {
    const nonceRequest: NonceRequest = new NonceRequest(publicKey);
    this.authService.getNonce(nonceRequest).subscribe({
      next: (response: { nonce: string }) => {
        this.signNonce(publicKey, response.nonce);
      },
      error: (error) => {
        this.resetWalletConnection();
        console.error("Error getting nonce:", error);
        this.notification.error("Error getting nonce", "Could Not Get Nonce");
      },
    });
  }

  /**
   * Signs the nonce with the wallet and sends the signature to the backend for authentication.
   *
   * @param publicKey The pubKey of the wallet
   * @param nonce The nonce to sign
   */
  signNonce(publicKey: string, nonce: string) {
    this.authService.signNonce(nonce).then(({signature, status}) => {
      if (status === 'timeout') {
        this.notification.info("Late Signature", "You signed the message, but it was after the timeout.");
      } else if (status === 'success' && signature) {
        const authRequest: AuthRequest = new AuthRequest(publicKey, signature);
        this.authenticateUser(authRequest);
      }
    })
      .catch(({status, error}) => {
        if (status === 'timeout') {
          this.notification.error("Timeout", "Signing nonce took too long");
        } else if (status === 'error') {
          console.error("Error signing nonce:", error);
          this.notification.error("Error signing nonce", "Could Not Sign Nonce");
        }
        this.resetWalletConnection();
      });
  }


  /**
   * Authenticates the user by sending the signature of the signed nonce to the backend
   *
   * @param authRequest signature of the signed nonce
   */
  authenticateUser(authRequest: AuthRequest) {
    this.authService.loginUser(authRequest).subscribe({
      next: (response) => {
        this.walletService.connectWallet();
        this.notification.success("Login successful", "Logged in");
      },
      error: (error) => {
        this.resetWalletConnection();
        console.error("Login failed, error:", error);
        this.error = true;
        if (typeof error.error === "object") {
          this.errorMessage = error.error.error;
        } else {
          this.errorMessage = error.error;
        }
      },
    });
  }

  /**
   * Disconnects the wallet and logs out the user.
   */
  logoutUser() {
    this.walletService.disconnectWallet().then(r => {
      this.resetWalletConnection();
      this.closeModal();
      this.notification.success("Logout successful", "Logged out");
    });
  }

  resetWalletConnection() {
    this.walletService.disconnectWallet();
    this.publicKey = "";
    this.balance = 0;
  }

  formatWalletAddress(address: string): string {
    return `${address.slice(0, 4)}...${address.slice(-4)}`;
  }

  openWalletModal() {
    this.modalService.open(this.walletModal);
  }

  openConnectModal() {
    this.modalService.open(this.connectModal);
  }

  closeModal() {
    this.modalService.dismissAll();
  }

  vanishError() {
    this.error = false;
  }
}
