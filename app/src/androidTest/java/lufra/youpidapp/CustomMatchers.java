package lufra.youpidapp;

import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.SpannedString;
import android.text.style.UnderlineSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.appcompat.widget.Toolbar;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.matcher.BoundedMatcher;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

import static org.hamcrest.Matchers.is;

public class CustomMatchers {

    // Drawable

    public static Matcher<View> withDrawable(final int resourceId) {
        return new DrawableMatcher(resourceId);
    }

    public static Matcher<View> noDrawable() {
        return new DrawableMatcher(-1);
    }

    // Toolbar

    public static Matcher<View> withToolbarTitle(CharSequence title) {
        return withToolbarTitle(is(title));
    }

    public static Matcher<View> withToolbarTitle(final Matcher<CharSequence> textMatcher) {
        return new BoundedMatcher<View, Toolbar>(Toolbar.class) {
            @Override
            public boolean matchesSafely(Toolbar toolbar) {
                return textMatcher.matches(toolbar.getTitle());
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("with toolbar title: ");
                textMatcher.describeTo(description);
            }
        };
    }

    public static Matcher<View> withNbOfMenuItemInToolbar(final int numberMenuItems) {
        return new BoundedMatcher<View, Toolbar>(Toolbar.class) {
            @Override
            protected boolean matchesSafely(Toolbar item) {
                Menu menu = item.getMenu();
                return menu.size() == numberMenuItems;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("with toolbar of ");
                description.appendValue(numberMenuItems);
                description.appendText(" menu items");
            }
        };
    }

//    public static Matcher<View> withIconMenuItemInToolbar(final int menuItemId, Drawable drawable) {
//        return new BoundedMatcher<View, Toolbar>(Toolbar.class) {
//            @Override
//            protected boolean matchesSafely(Toolbar item) {
//                Menu menu = item.getMenu();
//                if (menu.findItem(menuItemId) == null) {
//                    throw new NoMatchingViewException.Builder()
//                            .withCause(
//                                    new RuntimeException("Menu [MenuItem: " + menuItemId + "] not present in Menu " + menu)
//                            )
//                            .build();
//                }
//                MenuItem mi = menu.getItem(menuItemId);
//                return menu.size() == menuItemId;
//            }
//
//            @Override
//            public void describeTo(Description description) {
//                description.appendText("with toolbar of ");
//                description.appendValue(menuItemId);
//                description.appendText(" menu items");
//            }
//        };
//    }

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
