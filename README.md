
![Logo](https://i.ibb.co/B36Kfcj/Group-395.png)


# AniCafe


The AniCafe app was developed to help anime fans organize their own anime lists. The name "AniCafe" is a combination of the words "Ani" for anime and "Cafe", which on the one hand stands for a pleasant and feel-good ambience and on the other hand alludes to the user's taste, which we try to find out through recommendations and our Random-Anime-Generator.
The app accesses the popular MyAnimeList.net website through an API, allowing users to manage their existing user accounts and access the extensive anime database. By implementing a modernized UI, engaging animations, and an exciting user interaction experience, AniCafe offers a more attractive and user-friendly experience than before. Our focus on modern UI and animations makes the app a preferred alternative to the official website/app for anime fans.
### Recommended emulator
- Smartphone, as large as possible: Recommended is - Pixel 6 Pro
- System image, as new as possible: Recommended is - API Level 33 / Tiramisu / Android 13.0
  
### Login Data
To make things easier for you, we have created a MyAnimeList account for you, which already has anime saved in the list.

```bash
username: anicafe_test
password: AniCafe!23
```

#### How to login:
Click the Login button and wait for the login page to appear, then enter your details and click Login. Now it is important to wait for allow to appear and click on it only once. This will take you to our App-Startpage. You are now logged in and stored in the store -> no further login is required unless you logout. 


## Architekturübersicht
AniCafe implements the MVVM (Model View ViewModel) architectural pattern. Thanks to the clear separation of model and view, we were able to experiment extensively with the individual components, especially the view, without having to change the entire model. Our design components emerged from spontaneous ideas and interesting video/tutorial suggestions, which allowed us to make many changes. This approach gave us clear advantages. In addition, a clear code base, especially for a more complex design like ours, was very important.

To communicate with the API, we used Retrofit as a model. This affects both the model for representing the data received and the ViewModel for making API requests, without affecting the view.

---

In practice, our MVVM is structured as follows:

```bash
Folder:
    viewModel   (ViewModel)
    ui          (View)
    network     (Model as Repository)
```
### Extern Libraries

- [RecyclerView Animations](https://github.com/wasabeef/recyclerview-animators)
    - This library gives us the opportunity to turn boring RecyclerViews into an engaging user experience. We have used the "Scale Animation" to emphasise a "poppy" feeling when scrolling the personal anime list. In addition, we have a kind of "swipe" when deleting the anime from the list.

- [LoadingButton](https://github.com/leandroBorgesFerreira/LoadingButtonAndroid)
    - This library offers us to turn a button into a "loading" button when clicked, we have this function on the detailed anime page where you can add the anime to your personal list with one click. With this we want to emphasise a reactive "loading" feeling.

- [PopupDialog](https://github.com/wasabeef/recyclerview-animators)
    - This library allows us to quickly implement pop-ups. For example, we have used this for the logout process to ask for confirmation of the process.

- [PowerSpinner](https://github.com/skydoves/PowerSpinner)
    - This library allows us to switch from the typical Android dropdown to a responsive and customisable modern dropdown. We used it for our list and popup (edit anime).

- [CircleImageView](https://github.com/hdodenhof/CircleImageView)
    - As it is difficult to implement a simple round image, we used this library.

- [Retrofit](https://github.com/square/retrofit)
    - Through the library, we have a simpler handling integration of API-Calls, which has made our work a lot easier.

- [Glide](https://github.com/bumptech/glide)
    - This library helped us to load the images/GIFs from the API. The cache function, which also loads only the resolution we need, was very important for us.

- [Mal4J](https://github.com/KatsuteDev/Mal4J)
    - A Java library specifically for interacting with the MyAnimeList API, which allows us to manipulate the API using methods (instead of many API requests).

- [MPAndroidCharts](https://github.com/PhilJay/MPAndroidChart)
    - We used the data visualisation library to create a pie chart in the user profile showing the different statuses of the user.
### References to used code

- [PieChart for profile](https://youtu.be/fsVdzURuo_Y)
- [Shake Detection for random anime](https://youtu.be/fPa9Sev7il8)
- [MAL WebView authorization](https://stackoverflow.com/questions/69686648/oauth2-authorization-to-my-anime-list-not-working)
- [Picking the dominant color for home background](https://stackoverflow.com/questions/8471236/finding-the-dominant-color-of-an-image-in-an-android-drawable) 
