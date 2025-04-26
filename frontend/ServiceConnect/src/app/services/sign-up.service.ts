import { Injectable } from '@angular/core';
import { User } from '../models/User';
@Injectable({
  providedIn: 'root'
})
export class SignUpService {

  constructor() { }

  handleSignUp(user: User): void {
    const previousUsers = localStorage.getItem('users');
    const users = previousUsers ? JSON.parse(previousUsers) : [];
    users.push(user);
    localStorage.setItem('users', JSON.stringify(users));
  }
  
}
