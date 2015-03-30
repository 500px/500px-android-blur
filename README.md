## 500px Android Blurring View

For more information, please see [our blog post](http://developers.500px.com/2015/03/17/a-blurring-view-for-android.html).

### Usage

First, give the blurring view a reference to the view to be blurred:

``` java
blurringView.setBlurredView(blurredView);
```

and then whenever the blurred view changes, invalidate the blurring view:

``` java
blurringView.invalidate();
```

### Demo

![500px Blurring View Demo](blurdemo.gif "500px Blurring View Demo")

### License

This project is licensed under the terms of [the MIT license](LICENSE.txt).
