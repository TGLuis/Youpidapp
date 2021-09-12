package lufra.youpidapp;

import android.content.res.Resources;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Checkable;
import android.widget.SeekBar;

import androidx.appcompat.widget.Toolbar;
import androidx.test.espresso.PerformException;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.espresso.util.HumanReadables;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayingAtLeast;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.isA;

public class CustomViewActions {

    // Toolbar

    public static ViewAction clickToolbarItem(final int menuItemId) {
        return new ViewAction() {

            @Override
            public void perform(UiController uiController, View view) {
                Toolbar toolbar = (Toolbar) view;
                Menu menu = toolbar.getMenu();
                if (null == menu.findItem(menuItemId)) {
                    throw new PerformException.Builder()
                            .withActionDescription(this.getDescription())
                            .withViewDescription(HumanReadables.describe(view))
                            .withCause(new RuntimeException(getErrorMessage(menu, view)))
                            .build();
                }
                MenuItem mi;

                menu.performIdentifierAction(menuItemId, 0);
            }

            private String getErrorMessage(Menu menu, View view) {
                String newLine = System.getProperty("line.separator");
                StringBuilder errorMessage =
                        new StringBuilder("Menu item was not found, " + "available menu items:")
                                .append(newLine);
                for (int position = 0; position < menu.size(); position++) {
                    errorMessage.append("[MenuItem] position=").append(position);
                    MenuItem menuItem = menu.getItem(position);
                    if (menuItem != null) {
                        CharSequence itemTitle = menuItem.getTitle();
                        if (itemTitle != null) {
                            errorMessage.append(", title=").append(itemTitle);
                        }
                        if (view.getResources() != null) {
                            int itemId = menuItem.getItemId();
                            try {
                                errorMessage.append(", id=");
                                String menuItemResourceName = view.getResources().getResourceName(itemId);
                                errorMessage.append(menuItemResourceName);
                            } catch (Resources.NotFoundException nfe) {
                                errorMessage.append("not found");
                            }
                        }
                        errorMessage.append(newLine);
                    }
                }
                return errorMessage.toString();
            }

            @Override
            public Matcher<View> getConstraints() {
                return allOf(
                        isAssignableFrom(Toolbar.class),
                        withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE),
                        isDisplayingAtLeast(90));
            }

            @Override
            public String getDescription() {
                return "Click on the " + menuItemId + " item in the Toolbar menu";
            }
        };
    }

    // Seekbar

    public static ViewAction setProgress(final int progress) {
        return new ViewAction() {
            @Override
            public void perform(UiController uiController, View view) {
                SeekBar seekBar = (SeekBar) view;
                seekBar.setProgress(progress);
            }
            @Override
            public String getDescription() {
                return "Set a progress on a SeekBar";
            }
            @Override
            public Matcher<View> getConstraints() {
                return ViewMatchers.isAssignableFrom(SeekBar.class);
            }
        };
    }

    // Checkbox

    public static ViewAction setChecked(final boolean checked) {
        return new ViewAction() {
            @Override
            public BaseMatcher<View> getConstraints() {
                return new BaseMatcher<View>() {
                    @Override
                    public boolean matches(Object item) {
                        return isA(Checkable.class).matches(item);
                    }

                    @Override
                    public void describeMismatch(Object item, Description mismatchDescription) {}

                    @Override
                    public void describeTo(Description description) {}
                };
            }

            @Override
            public String getDescription() {
                return null;
            }

            @Override
            public void perform(UiController uiController, View view) {
                Checkable checkableView = (Checkable) view;
                checkableView.setChecked(checked);
            }
        };
    }
}
