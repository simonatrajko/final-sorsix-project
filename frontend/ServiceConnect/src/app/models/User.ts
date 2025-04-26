export class User {
    id: number;
    username: string;
    fullName:string;
    email: string;
    password: string;
    location: string;
    role: string;
    pictureUrl?: string;
  
    constructor(
      id: number=0,
      username: string="",
      email: string="",
      password: string="",
      location: string="",
      role: string="",
      pictureUrl: string="",
      fullname:string=""
    ) {
      this.id = id;
      this.username = username;
      this.email = email;
      this.password = password;
      this.location = location;
      this.role = role;
      this.pictureUrl = pictureUrl;
      this.fullName=fullname;
    }
  }
  