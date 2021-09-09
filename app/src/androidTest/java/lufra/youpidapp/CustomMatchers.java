package lufra.youpidapp;

import android.graphics.Typeface;
import android.text.SpannedString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.test.espresso.matcher.BoundedMatcher;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

public class CustomMatchers {

    // Drawable

    public static Matcher<View> withDrawable(final int resourceId) {
        return new DrawableMatcher(resourceId);
    }

    public static Matcher<View> noDrawable() {
        return new DrawableMatcher(-1);
    }

    // Seekbar

    public static Matcher<View> withSeekbarProgress(final int expectedProgress) {
        return new BoundedMatcher<View, AppCompatSeekBar>(AppCompatSeekBar.class) {
            @Override
            public void describeTo(Description description) {
                description.appendText("expected: ");
                description.appendText("" + expectedProgress);
            }
            @Override
            public boolean matchesSafely(AppCompatSeekBar seekBar) {
                return seekBar.getProgress() == expectedProgress;
            }
        };
    }

    // Styles

    public static Matcher<View> withNormalStyle() {
        return new BoundedMatcher<View, TextView>(TextView.class) {
            public boolean matchesSafely(TextView textView) {
                return (textView.getTypeface().getStyle() == Typeface.NORMAL);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("has Normal Text");
            }
        };
    }

    public static Matcher<View> withItalicStyle() {
        return new BoundedMatcher<View, TextView>(TextView.class) {
            @Override
            public boolean matchesSafely(TextView textView) {
                return (textView.getTypeface().getStyle() == Typeface.ITALIC);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("has Italic Text" );
            }
        };
    }

    public static Matcher<View> withBoldStyle() {
        return new BoundedMatcher<View, TextView>(TextView.class) {
            @Override
            public boolean matchesSafely(TextView textView) {
                return (textView.getTypeface().getStyle() == Typeface.BOLD);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("has Bold text");
            }
        };
    }

    public static Matcher<View> withBoldItalicStyle() {
        return new BoundedMatcher<View, TextView>(TextView.class) {
            @Override
            public boolean matchesSafely(TextView textView) {
                return (textView.getTypeface().getStyle() == Typeface.BOLD_ITALIC);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("has Bold and Italic text");
            }
        };
    }

    public static Matcher<View> withUnderlinedText() {
        return new BoundedMatcher<View, TextView>(TextView.class) {
            @Override
            protected boolean matchesSafely(TextView textView) {
                CharSequence charSequence = textView.getText();
                UnderlineSpan[] underlineSpans = ((SpannedString) charSequence).getSpans(0, charSequence.length(), UnderlineSpan.class);

                return underlineSpans != null && underlineSpans.length > 0;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("has Underlined Text");
            }
        };
    }
}
