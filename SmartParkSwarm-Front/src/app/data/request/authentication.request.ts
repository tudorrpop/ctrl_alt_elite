
export class AuthenticationRequest {
    username: string;
    password: string;
    role: string = "ADMIN";
  
    constructor(username: string, password: string) {
      this.username = username;
      this.password = password;
    }
}