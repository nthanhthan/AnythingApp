package com.example.anythingapp

sealed class NavigationItem(var route:String ,var icon: Int, var title:String){
    object Home: NavigationItem(route = "home",R.drawable.ic_baseline_home_24,"Home")
    object Music: NavigationItem(route = "music",R.drawable.ic_baseline_music_note_24,"Music")
    object Movies: NavigationItem(route = "Movie",R.drawable.ic_baseline_movie_24,"Movie")
    object Books: NavigationItem(route = "Book",R.drawable.ic_baseline_book_24,"Book")
    object Profile: NavigationItem(route = "Profile",R.drawable.ic_baseline_person_24,"Profile")

}
