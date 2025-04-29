import { Component } from '@angular/core';
import { UserProfileComponent } from '../user-profile/user-profile.component';
@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css'],
  imports:[UserProfileComponent]
})
export class HomeComponent {
  
}
