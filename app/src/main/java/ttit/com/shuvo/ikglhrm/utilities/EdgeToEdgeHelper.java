package ttit.com.shuvo.ikglhrm.utilities;

import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import androidx.activity.ComponentActivity;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

public class EdgeToEdgeHelper {

    private EdgeToEdgeHelper() {}
    /**
     * Call before setContentView().
     */
    public static void enable(@NonNull ComponentActivity activity) {
        EdgeToEdge.enable(activity);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            activity.getWindow()
                    .setNavigationBarContrastEnforced(false);
        }
    }

    public static void applyInsets(
            @NonNull ComponentActivity activity,
            @NonNull View rootView,
            @NonNull View imeAwareView,
            @NonNull LinearLayout centeredContent,
            boolean darkStatusBarIcons,
            boolean darkNavigationBarIcons
    ) {

        WindowInsetsControllerCompat controller = WindowCompat.getInsetsController(
                        activity.getWindow(),
                        activity.getWindow().getDecorView());

        controller.setAppearanceLightStatusBars(darkStatusBarIcons);
        controller.setAppearanceLightNavigationBars(darkNavigationBarIcons);

        final int initialLeft = rootView.getPaddingLeft();
        final int initialTop = rootView.getPaddingTop();
        final int initialRight = rootView.getPaddingRight();
        final int initialBottom = rootView.getPaddingBottom();

        final int imeLeft = imeAwareView.getPaddingLeft();
        final int imeTop = imeAwareView.getPaddingTop();
        final int imeRight = imeAwareView.getPaddingRight();
        final int imeBottom = imeAwareView.getPaddingBottom();

        ViewCompat.setOnApplyWindowInsetsListener(
                rootView,
                (view, windowInsets) -> {

                    int insetTypes =
                            WindowInsetsCompat.Type.systemBars()
                                    | WindowInsetsCompat.Type.displayCutout();

                    Insets safeInsets =
                            windowInsets.getInsets(insetTypes);

                    Insets imeInsets = windowInsets.getInsets(
                            WindowInsetsCompat.Type.ime()
                    );

                    boolean imeVisible = windowInsets.isVisible(
                            WindowInsetsCompat.Type.ime()
                    );

                    view.setPadding(
                            initialLeft + safeInsets.left,
                            initialTop + safeInsets.top,
                            initialRight + safeInsets.right,
                            initialBottom
                    );

                    int requiredBottomInset = Math.max(
                            safeInsets.bottom,
                            imeInsets.bottom
                    );

                    imeAwareView.setPadding(
                            imeLeft,
                            imeTop,
                            imeRight,
                            imeBottom + requiredBottomInset
                    );

                    int requiredGravity = imeVisible
                            ? Gravity.TOP
                            : Gravity.CENTER_VERTICAL;

                    if (centeredContent.getGravity()
                            != requiredGravity) {

                        centeredContent.setGravity(
                                requiredGravity
                        );
                    }

                    return windowInsets;
                }
        );

        ViewCompat.requestApplyInsets(rootView);
    }
}
