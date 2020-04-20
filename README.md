## [Currently unmaintained]

# 500px Android Blurring View

For more information, please see [our blog post](http://developers.500px.com/2015/03/17/a-blurring-view-for-android.html).

## Download

Define via Gradle:

``` groovy
repositories {
    maven { url 'https://github.com/500px/500px-android-blur/raw/master/releases/' }
}

dependencies {
    compile 'com.fivehundredpx:blurringview:1.0.0'
}
```

Enable renderscript in your module's `defaultConfig`:
```groovy
android {
    defaultConfig {

        renderscriptTargetApi 21
        renderscriptSupportModeEnabled true
        ...
    }
}

```


## Usage

First, give the blurring view a reference to the view to be blurred:

``` java
blurringView.setBlurredView(blurredView);
```

and then whenever the blurred view changes, invalidate the blurring view:

``` java
blurringView.invalidate();
```

## Demo

![500px Blurring View Demo](blurdemo.gif "500px Blurring View Demo")

## License

This project is licensed under the terms of [the MIT license](LICENSE.txt).
