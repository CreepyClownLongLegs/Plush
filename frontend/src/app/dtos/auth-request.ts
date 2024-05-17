export class AuthRequest {
  constructor(
    public publicKey: string,
    public signature: string
  ) {}
}
