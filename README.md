![fairytaleslogoAI](https://github.com/alexandengstrom/fairytales/assets/123507241/c6065965-bf4d-4d90-8874-2cb1a623724b)


Welcome to **F(ai)rytales**. This is an app for making bedtime stories with AI, made for children and their parents. The idea is to let users make their own characters and stories. The app lets you create characters and then make stories with them. You can pick where the story happens, either by writing a place or choosing on Google Maps. You also give ideas for the story plot.

The app was developed using Java in Android Studio. It follows the MVVM (Model-View-ViewModel) design pattern, which means it's organized into three main parts: the View, ViewModels, and Models. This project was my first time working with Java, so I learned the language as I went along building the app.

### Customization and Interaction
The app offers an interactive and user-friendly interface where you can:

* **Create Unique Characters**: You have the freedom to create a variety of characters for your stories. This can be real characters as yourself and your friend but i can also be made up character or historical persons.
* **Choose Your Setting**: Decide where the story should take place by writing a location, either a real one like your home town or a made up one.
* **Shape Your Story**: When creating a story you can also include some information about what you want the story to be about.

### Colors and Language Support
The app has two color themes: a normal theme and a dark mode. Dark mode activates automatically based on the device's settings.

The app supports English, German, French, Spanish, and Swedish languages. This means all text inside the app will be displayed in the preferred language, and the stories will also be generated in the chosen language. The language will be chosen automatically based on the device's settings. English will be used by default if the language is not supported.

### Installation
To get **F(ai)rytales** up and running on your local machine, follow these steps:

1. **Clone the repository:** Begin by cloning the repository to your local machine. Open a terminal and run the following command:
   ```
   git clone git@github.com:alexandengstrom/fairytales.git
   ```
   Navigate into the cloned repository:
      ```
   cd fairytales
   ```
2. **Setting up Google Services:** You need the google-services.json file for proper integration with Google services. Configure your app in the Firebase console, download the google-services.json file, and place it in the app/ directory.
3. **Configure API Keys in Local Properties:** In the app/ directory of the project, create a local.properties file and add your API keys:
   ```
   API_KEY   = "Your OpenAI API key here"
   MAPS_KEY  = "Your Google Maps API key here"
   ```
4. **Build and Run the Application:** Open the project in Android Studio. Android Studio should automatically recognize the Gradle files and set up the project accordingly. Once the setup is complete, you can run the application on an emulator or a physical device.

### Future Enhancements
One of the ideas i had for **F(ai)rytales** was to evolve the storytelling process into a more dynamic and interactive experience. The concept is to build the story in sections, rather than crafting the entire story in one go. At the end of each section, users will have the opportunity to choose from various paths, leading to different story developments. This approach is inspired by "choose your own adventure" games, adding another layer of interactivity to the storytelling process.

Another feature I would like to add is to generete an image to every story aswell. This could be solved by using the Dall-E API and should not be that hard to implement.

However, this project was part of the course TDP028 at Linköping University and due to the academic deadlines I did not have time to implement this functionality.

### Images
The following section will display some screenshots of the app.


<table>
  <tr>
    <td align="center">
      <b>Login</b><br>
      <img src="https://github.com/alexandengstrom/fairytales/assets/123507241/0df020e8-6ff4-459c-9628-26758fefa427" width="300px" alt="Login Screenshot"/><br>
    </td>
    <td align="center">
      <b>Register new account</b><br>
      <img src="https://github.com/alexandengstrom/fairytales/assets/123507241/893e44aa-34cd-43db-9169-713a309e8476" width="300px" alt="Register Screenshot"/><br>
    </td>
  </tr>
  <tr>
    <td align="center">
      <b>Creating a new character</b><br>
      <img src="https://github.com/alexandengstrom/fairytales/assets/123507241/43b4f51a-ce69-4ffc-8324-46060171206b" width="300px" alt="New Character Screenshot"/><br>
    </td>
    <td align="center">
      <b>Generating a new story</b><br>
      <img src="https://github.com/alexandengstrom/fairytales/assets/123507241/deb2573f-9a90-4437-9637-aaf89e991518" width="300px" alt="New Story Screenshot"/><br>
    </td>
  </tr>
  <tr>
    <td align="center">
      <b>Display of all stories created</b><br>
      <img src="https://github.com/alexandengstrom/fairytales/assets/123507241/3d999995-f47d-48eb-85d7-66ed8b2b1d10" width="300px" alt="Stories List Screenshot"/><br>
    </td>
    <td align="center">
      <b>Reading one story</b><br>
      <img src="https://github.com/alexandengstrom/fairytales/assets/123507241/d92c4a0e-d72d-4dea-9dbd-47d7cf954ebe" width="300px" alt="Reading Story Screenshot"/><br>
    </td>
  </tr>
</table>



