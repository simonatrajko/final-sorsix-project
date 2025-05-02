import { Component } from '@angular/core';
import { UserProfileComponent } from '../user-profile/user-profile.component';
import {UserService} from "../../services/user.service";
import { Router, RouterLink } from '@angular/router';
import { Subscription } from 'rxjs';
@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css'],
  imports:[UserProfileComponent,RouterLink]
})
export class HomeComponent {
  private subscription: Subscription = new Subscription();
  isLoading = true;

  constructor(private userService: UserService, private router: Router) {}

  ngOnInit() {
    this.subscription = this.userService.currentUser$.subscribe(user => {
      this.isLoading = false;
      if (user) {
        this.router.navigateByUrl(`/user/${user.username}`);
      }
    });
  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }
}
