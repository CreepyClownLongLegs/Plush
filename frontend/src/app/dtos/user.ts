export class UserDetailDto {
    publicKey: string;
    firstname: string;
    lastname: string;
    emailAddress: string;
    phoneNumber: string;
    locked: boolean;
    country: string;
    postalCode: string;
    city: string;
    addressLine1: string;
    addressLine2: string;
    admin: boolean;
}

export class UserListDto {
  publicKey: string;
  firstname: string;
  lastname: string;
  admin: boolean;
}
