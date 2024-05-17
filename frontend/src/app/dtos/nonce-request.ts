import {PublicKey} from "@solana/web3.js";

export class NonceRequest {
  constructor(
    public publicKey: string,
  ) {}
}
