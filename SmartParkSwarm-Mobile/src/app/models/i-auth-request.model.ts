export interface IAuthRequest {
  username: string;
  password: string;
  role: 'CUSTOMER' | 'ADMIN';
}
