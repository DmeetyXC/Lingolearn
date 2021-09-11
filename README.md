# Lingolearn
Android application for learning different languages.  
This application downloads text and pictures books from Cloud Firestore. After that, book parsed by sentences. Parsing takes into account density of pixels per inch device. This ensures that optimal number of characters will be displayed on different devices. For asynchronous work used Coroutines, and Flow used to check network access. Glide is used for loading and processing images. Room is used to store already loaded books and parameters for reading book, dictionary and other information.

The list of books uses a vertical RecyclerView with the ability to scroll horizontally through categories of books.
When reading book, screen is divided into two parts: native and learning languages. Display style book changes depending on orientation of device. When you click on text, a sentence in two languages ​​is selected. When you click on offer below, the screen automatically scrolls. Navigation between pages takes place through navigation buttons at bottom. A long press on text switches to text mode, in which you can select text and add it to dictionary. Moreover, before being added to dictionary, this word is translated using Microsoft Translator API through the Retrofit library. There is also support for adding book to list of favorites.

This application uses MVVM architecture. Navigating app using Jetpack Navigation, and dependency injection using Dagger Hilt. LiveData is used to subscribe changes books and other data from database.  
Application support changing dark and light themes, changing size of text and languages ​​studied.  
To build project, you need add gradle.properties file to which add line: apiTranslate = "your_key". Can get key for free on Microsoft website, read more https://docs.microsoft.com/en-us/azure/cognitive-services/translator/translator-how-to-signup  
Link to application in Google Play Market 
https://play.google.com/store/apps/details?id=com.dmeetyxc.lingolearn