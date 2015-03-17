# 500px Android Blurring View

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

