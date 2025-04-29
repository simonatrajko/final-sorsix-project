import { Injectable } from '@angular/core';
import { User } from '../models/User';
import { Provider } from '../models/Provider';

@Injectable({
  providedIn: 'root'
})
export class SignUpService {

  constructor() { }

  handleSignUp(user: User): void {
    const previousUsers = localStorage.getItem('users');
    const users = previousUsers ? JSON.parse(previousUsers) : [];
    if(user.role=="provider"){
      let provider=user as Provider
      provider.services=[]
      users.push(provider)
    }
    else{
      users.push(user);
    }
    localStorage.setItem('users', JSON.stringify(users));
  }
  
}
