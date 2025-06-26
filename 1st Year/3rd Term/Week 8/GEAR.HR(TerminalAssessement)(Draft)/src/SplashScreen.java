import javax.swing.*;
import java.awt.*;

/**
 * SplashScreen class provides an animated loading screen
 * Displays company logo with gradient background before the main application loads
 * Note: All information in this program are sample data for demonstration purposes
 */
public class SplashScreen {
    // Splash screen configuration
    private static final int SPLASH_WIDTH = 500;
    private static final int SPLASH_HEIGHT = 350;
    private static final int LOGO_SIZE = 200;
    private static final int DISPLAY_DURATION = 3000; // 3 seconds

    // Application color scheme
    private static final Color GRADIENT_START = new Color(93, 224, 230);
    private static final Color GRADIENT_END = new Color(0, 74, 173);
    private static final Color TEXT_WHITE = Color.WHITE;
    private static final Color TEXT_GREY = new Color(180, 180, 180);

    /**
     * Displays the splash screen with company logo and loading animation
     * Automatically closes after 3 seconds and executes the provided callback
     * @param onFinish Callback function to execute when splash screen closes
     */
    public static void showSplash(Runnable onFinish) {
        JWindow splashWindow = createSplashWindow();
        JPanel mainPanel = createMainPanel();
        
        // Add content to splash window
        splashWindow.getContentPane().add(mainPanel);
        splashWindow.setVisible(true);

        // Set timer to close splash screen
        Timer closeTimer = createCloseTimer(splashWindow, onFinish);
        closeTimer.start();
    }

    /**
     * Creates and configures the splash window
     */
    private static JWindow createSplashWindow() {
        JWindow splashWindow = new JWindow();
        splashWindow.setSize(SPLASH_WIDTH, SPLASH_HEIGHT);
        splashWindow.setLocationRelativeTo(null);
        splashWindow.setAlwaysOnTop(true);
        return splashWindow;
    }

    /**
     * Creates the main panel with gradient background and content
     */
    private static JPanel createMainPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                
                // Create gradient background
                GradientPaint gradient = new GradientPaint(
                    0, 0, GRADIENT_START,
                    getWidth(), 0, GRADIENT_END
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.dispose();
            }
        };
        mainPanel.setOpaque(false);

        // Create and add content
        JPanel contentPanel = createContentPanel();
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        return mainPanel;
    }

    /**
     * Creates the content panel with logo and loading text
     */
    private static JPanel createContentPanel() {
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setOpaque(false);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        // Add logo
        JLabel logoLabel = createLogoLabel();
        contentPanel.add(logoLabel, BorderLayout.CENTER);

        // Add loading text
        JLabel loadingLabel = createLoadingLabel();
        contentPanel.add(loadingLabel, BorderLayout.SOUTH);

        return contentPanel;
    }

    /**
     * Creates the logo label with company logo or fallback text
     */
    private static JLabel createLogoLabel() {
        JLabel logoLabel = new JLabel();
        try {
            // Load and resize company logo
            ImageIcon logoIcon = new ImageIcon("Logo/Icon.png");
            Image logoImage = logoIcon.getImage();
            Image resizedLogo = logoImage.getScaledInstance(LOGO_SIZE, LOGO_SIZE, Image.SCALE_SMOOTH);
            ImageIcon resizedIcon = new ImageIcon(resizedLogo);
            logoLabel.setIcon(resizedIcon);
        } catch (Exception e) {
            // Fallback to text if logo not found
            logoLabel.setText("GEAR.HR");
            logoLabel.setFont(new Font("Garet", Font.BOLD, 32));
            logoLabel.setForeground(TEXT_WHITE);
        }
        
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        logoLabel.setVerticalAlignment(SwingConstants.CENTER);
        return logoLabel;
    }

    /**
     * Creates the loading text label
     */
    private static JLabel createLoadingLabel() {
        JLabel loadingLabel = new JLabel("Loading...");
        loadingLabel.setFont(new Font("Garet", Font.PLAIN, 14));
        loadingLabel.setForeground(TEXT_WHITE);
        loadingLabel.setHorizontalAlignment(SwingConstants.CENTER);
        return loadingLabel;
    }

    /**
     * Creates a timer to close the splash screen after specified duration
     */
    private static Timer createCloseTimer(JWindow splashWindow, Runnable onFinish) {
        Timer timer = new Timer(DISPLAY_DURATION, e -> {
            splashWindow.setVisible(false);
            splashWindow.dispose();
            if (onFinish != null) {
                onFinish.run();
            }
        });
        timer.setRepeats(false);
        return timer;
    }
} 