import java.util.Scanner;
import java.util.ArrayList;
import java.util.Random;

import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineEvent;

import java.util.HashMap;

class Objects {
    String name;
    String clue;
    boolean clues;
    boolean locked;
    boolean interactable;
    boolean notecol;
    boolean onetime;
    boolean visited;
    boolean scene;

    Objects(String name, String clue, boolean clues,
            boolean locked, boolean interactable, boolean notecol,
            boolean onetime, boolean visited, boolean scene) {
        this.name = name;
        this.clue = clue;
        this.clues = clues;
        this.locked = locked;
        this.interactable = interactable;
        this.notecol = notecol;
        this.onetime = onetime;
        this.visited = visited;
        this.scene = scene;
    }
}

public class App {

    public static boolean Game = true;
    public static boolean unlock = false;
    public static boolean interactable = true;
    public static boolean visited = false;
    public static int x = 0, y = 0;
    public static boolean pathattempt = false;
    public static boolean hasdiary = false;
    public static Clip bgmusic;
    public static boolean continuing = false;
    public static ArrayList<String> inventory = new ArrayList<>();
    public static boolean deads = false;
    public static boolean readdiary = false;
    public static boolean scene6;
    public static boolean chapel = false;
    public static boolean gameover = false;
    public static boolean bad = false;
    public static boolean good = false;
    public static boolean secret = false;

    // OBJECTS //

    static HashMap<String, Objects> objects = new HashMap<>();

    static Objects note = new Objects("Note",
            "There was once a chapel straight ahead of here. It has a cemetery beside it.\n" +
                    "It can be accessed from the road outside. It was a place of peace — a place of serenity.\n" +
                    "Find the road. From there, you might be able to reach the nearest town.\n",
            true, unlock, !interactable, false, false, visited, false);
    

    static Objects Chapel = new Objects("Chapel", "", false, unlock, interactable, false, false, visited, true);

    static Objects road = new Objects("Road", "", false, unlock, interactable, false, false, visited, true);
    

    static {
        objects.put("0,1", note);
        objects.put("0,9", Chapel);
        objects.put("-20,0", road); // remove x, any x
    }



    static void jumpscare() {
        player("jumpscare.wav", 2f, false);
    }

    static void wait(int milisecs) {

        try {
            Thread.sleep(milisecs);
        } catch (Exception e) {
            Thread.currentThread().interrupt();
        }
    }

    static void player(String audio, float volume, boolean loop) {
        try {
            File music = new File("Audio/" + audio);

            if (music.exists()) {
                AudioInputStream audioin = AudioSystem.getAudioInputStream(music);
                Clip clip = AudioSystem.getClip();
                clip.open(audioin);
                FloatControl vol = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                vol.setValue(volume);
                if (loop) {
                    clip.loop(Clip.LOOP_CONTINUOUSLY);
                    bgmusic = clip;
                }
                clip.start();

                clip.addLineListener(event -> {
                    if (event.getType() == LineEvent.Type.STOP) {
                        clip.close();
                    }
                });

            } else {
                System.out.println("error, cant find file");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    static void typewriter(String input) {
        for (int i = 0; i < input.length(); i++) {
            System.out.print(input.charAt(i));

            System.out.flush();

            if (i + 1 < input.length() && input.charAt(i) == ',') {
                wait(250);
            } else if (i + 1 < input.length() && input.charAt(i) == '.') {
                wait(350);
            } else
                wait(25);
        }
    }

    static void spawn(Scanner scanner) { // scene 1
        typewriter("You woke up in a dark forest.\n");
        typewriter("You remember nothing.\n");
        typewriter("Your head hurts like hell, not even comparable to a hammer being struck in it.\n");
        typewriter("Despite that you pulled yourself together... You got up.\n");
        typewriter("And you see a crashed car with a note beside it.\n");
    }

    static void moveForward(Scanner scanner, int stepCount, String message, boolean footstepsbehind, boolean coordinate, boolean addtox) {
        int steps = 0;
        while (steps != stepCount) {
            String input = scanner.nextLine();
            if (input.equals("w")) {
                player("footstep2.wav", -5f, false);
                steps++;

                if (addtox)
                    x++;
                else
                    y++;

                if (coordinate) {
                    System.out.println("Position: " + x + ", " + y);
                }
                if (footstepsbehind) {
                    wait(500);
                    player("footstep2big.wav", -5f, false);
                }
            } else
                System.out.println(message);

        }
    }

    static void moveForwardWithHim(Scanner scanner, int stepCount, String message, int hisx, int hisy,
            boolean lookBack, float hisVol) {
        Random random = new Random();
        int steps = 0;

        while (steps != stepCount) {
            String input = scanner.nextLine();
            if (input.equals("w")) {
                player("footstep2.wav", -5f, false);
                System.out.println("Position: " + x + ", " + y);
                wait(500);
                hisy += 2;
                System.out.println("His Position: " + hisx + ", " + hisy);
                player("footstep2big.wav", hisVol, false);
                steps++;
                y++;
            } else if (lookBack && input.equals("f")) {
                typewriter("I told you not to look back.\n");
                wait(random.nextInt(1500));
                jumpscare();
                wait(2000);

                Game = false;
                deads = true;
                break;
            } else
                System.out.println(message);
        }
    }

    static void scenario6(Scanner scanner) {

        try { // Try to stop music
            bgmusic.stop();
        } catch (Exception e) {
            System.out.println("something went wrong" + e);
        }

        if (continuing)
            typewriter("You returned to the path.\n");

        typewriter("Continue walking[w]\n");

        moveForward(scanner, 5, "There's no turning back now, the road is up ahead.", false, false, false);

        typewriter("The forest becomes weirdly familiar\n");
        wait(1000);

        System.out.println("Continue walking[w]\n");

        moveForward(scanner, 3, "There's no turning back now, the road is up ahead.", false, false, false);

        typewriter("That was the same rock\n");

        moveForward(scanner, 3, "There's no turning back now, the road is up ahead.", false, false, false);

        typewriter("That was the same clearing\n");

        moveForward(scanner, 3, "There's no turning back now, the road is up ahead.", false, false, false);

        typewriter("You remember seeing them all\n");

        moveForward(scanner, 3, "There's no turning back now, the road is up ahead.", false, false, false);

        typewriter("But you don't remember when\n");

        float vol = -10f;
        player("suspense.wav", 2f, true);

        System.out.println("Continue walking[w]\n");

        int hisx = x, hisy = y - 14;

        moveForwardWithHim(scanner, 3, "There's no turning back now, the road is up ahead.", hisx, hisy, false, vol++);

        hisy += 6;
        typewriter("That was not your footstep.\n");
        System.out.println("Press 'f' to look back");
        typewriter("Don't look back.\n");

        moveForwardWithHim(scanner, 3, "There's no turning back now, the road is up ahead.", hisx, hisy, true, vol++);
        if (Game == false || deads == true) {
            return;
        }

        typewriter("Keep walking\n");
        hisy += 6;

        moveForwardWithHim(scanner, 3, "There's no turning back now, the road is up ahead.", hisx, hisy, true, vol++);
        if (Game == false || deads == true) {
            return;
        }
        hisy += 6;

        typewriter("Keep walking\n");

        moveForwardWithHim(scanner, 3, "There's no turning back now, the road is up ahead.", hisx, hisy, true, vol++);
        if (Game == false || deads == true) {
            return;
        }
        hisy += 6;

        System.out.println("STOP.");
        bgmusic.stop();
        typewriter("He's right behind you.\n");
        wait(5000);
        player("breathing.wav", -2f, true);
        typewriter("Silence engulfed the forest\n");
        typewriter("You hear your own breath\n");
        typewriter(".....");
        wait(3000);
        bgmusic.stop();

        if (hasdiary && inventory.contains("diary")) {

            typewriter("You felt the diary in your pocket\n");
            typewriter("Read it? [y/n]\n");

            String ch;
            do {
                ch = scanner.nextLine();

                if (ch.equals("y")) {
                    diary(scanner);
                    readdiary = true;
                    break;
                }
            } while (!ch.equals("n"));

        }

        typewriter("You look ahead.\n");
        typewriter("The forest ends\n");
        typewriter("You can see a wide, dark river of black that stretches east all the way to the horizon\n");
        typewriter("You reached it.\n");
        typewriter("You reached the road\n");
        scene6 = true;
        return;
    }

    static void diary2(Scanner scanner) {
        Random random = new Random();
        boolean audioplayed = false;

        String[] diary = {
                "I woke up in the forest, and it was dark...",

                "I don't remember how I got here.",

                "I found a chapel.",

                "There was a road outside.",

                "I followed it.",

                "I ended up back at the chapel.",

                "page",

                "I tried again",

                "page",

                "I heard footsteps behind me",

                "page",

                "I didn't look",

                "page",

                "Then I looked",

                "page",

                "And I was-",

                "page",

                "And now...",

                "page",

                "You're...",

                "page",

                "Too...",

                "L..A..T..E",

        };

        String ch;

        ch = "";
        for (int i = 0; i < diary.length; i++) {

            if (diary[i].equals("page")) {
                System.out.println("Press 'd' for next page");
                do {
                    ch = scanner.nextLine();
                    if (!ch.equals("d"))
                        System.out.println("Press 'd' for next page");

                } while (!ch.equals("d"));

                typewriter("You turned to the next page\n");

            } else
                typewriter(diary[i] + "\n");

            if (!audioplayed) {
                int event = random.nextInt(5);
                if (event == 3) {
                    player("footstepsmuffled.wav", 5f, false);
                    audioplayed = true;
                }
            }

            wait(1000);

        }
        return;

    }

    static void chapel2(Scanner scanner) { // scene 2-2
        Random random = new Random();
        if (chapel)
            typewriter("You entered the chapel once again.\n");
        typewriter("You entered the chapel.\n");

        if (!readdiary && hasdiary) {
            typewriter("You once again felt the diary in your pocket\n");
            typewriter("Do you want to read it?[y/n] ");
            String ch;
            do {
                ch = scanner.nextLine();
                if (ch.equals("y")) {
                    diary2(scanner);
                    return;
                }
                System.out.println("invalid");
            } while (!ch.equals("n"));
        }

        if (!readdiary && !hasdiary) {
            typewriter("You see a diary at your foot.\n");
            typewriter("Do you wan't to read it?[y/n] ");
            String ch;
            do {
                ch = scanner.nextLine();
                if (ch.equals("y")) {
                    diary2(scanner);
                    break;
                } else
                    System.out.println("invalid");
            } while (!ch.equals("n"));
        }

        typewriter("You have nothing to do here now, leave the chapel.[press l]\n");
        String ch;
        ch = scanner.nextLine();
        if (ch.equals("l")) {
            return;
        } else {
            typewriter("I told you to leave...");
            random.nextInt(1000);
            jumpscare();
        }

    }

    static void scenario8Bad(Scanner scanner) {
        Random random = new Random();
        boolean scene = true;
        if (bgmusic != null) {
            bgmusic.stop();
        }

        int hisx = 0, hisy = 0;

        typewriter("But then... ");
        wait(2000);
        typewriter("The world goes silent...\n");
        wait(3000);

        String[] lines = {
                "The chapel is right in front of you....\n",
                "What... the... hell... just... happened...\n",
                "I was almost there.\n",
                "I can see the light.\n",
                "I can almost smell the pollution.\n",
                "Why are am I back here?\n",
                "You see the path you've walked on before. Take it[y] or enter the chapel[n]? [y/n]\n",

        };

        x = 0;
        y = 9;
        int line = 0;

        player("respawn.wav", 3f, false);
        System.out.println("Position: " + x + ", " + y);

        wait(1000);
        player("callofwitch.wav", 1f, true);

        // player returns to chapel
        while (scene) {

            typewriter(lines[line]);
            line++;

            if (line == 7) {
                String ch;
                do {
                    ch = scanner.nextLine();
                    if (ch.equals("n")) {
                        chapel2(scanner);
                        break;
                    } else if (ch.equals("y")) {
                        break;
                    } else
                        System.out.println("invalid");

                } while (!ch.equals("y"));
                break;
            }

            wait(1000);
        }

        typewriter("You have taken the path.\n");
        System.out.println("Move forward[w]\n");

        int steps = 0;
        boolean printed = false;

        while (steps != 2) {

            if (steps == 1 && !printed) {
                typewriter("WHY AM I BACK??????\n");
                wait(2000);
                System.out.println("Keep moving forward[w]");
                printed = true;
            }
            String input = scanner.nextLine();
            if (input.equals("w")) {
                player("footstep2.wav", -5f, false);
                y++;
            } else
                System.out.println("There's no turning back now, victory's up ahead?");

            if (x > 10 || y > 19) {
                x = 0;
                y = 9;
                System.out.println("Position: " + x + ", " + y);
                player("respawn.wav", 2.5f, false);
                steps++;
            }
        }

        typewriter("WHAT IS HAPPENING TO ME????\n");

        moveForward(scanner, 5, "There's no turning back now, victory's right up ahead", false, false, false);

        bgmusic.stop();
        System.out.println("STOP");
        typewriter("He's....");
        wait(1000);
        typewriter("Behind you now....\n");
        wait(1000);
        typewriter("Be.. quiet...\n");
        wait(1000);
        typewriter("Dont.. Move...\n");

        wait(1000);
        int delay = 900;
        float vol = -5f;

        while (hisy != 9) {
            System.out.println("His position: " + hisx + ", " + hisy);
            hisy++;
            player("footstep2big.wav", vol++, false);
            if (delay < 150)
                delay = 150;
            wait(delay);
            delay -= 150;
        }
        wait(random.nextInt(2500));

        jumpscare();

        wait(3100);
        Game = false;
        gameover = true;
        bad = true;
        return;
    }

    static void scenario8Good(Scanner scanner) {
        Random random = new Random();

        bgmusic.stop();
        typewriter("But then, the world gets darker...\n");
        wait(2000);
        typewriter("And darker...\n");
        wait(2000);
        typewriter("And darker...\n");
        wait(2000);
        typewriter("And you see a small speck of light in the distance.\n");
        wait(2000);
        System.out.println("Reach out to it[f]");

        String ch;
        do {
            ch = scanner.nextLine();
            if (ch.equals("f")) {
                break;
            } else
                System.out.println("Reach out to it[f]");
        } while (!ch.equals("f"));

        typewriter(".");
        wait(2000);
        typewriter(".");
        wait(2000);
        typewriter(".");
        wait(2000);

        typewriter("You smell pollution...\n");
        System.out.println("Open your eyes[f]");

        do {
            ch = scanner.nextLine();
            if (ch.equals("f")) {
                break;
            } else
                System.out.println("Open your eyes[f]");
        } while (!ch.equals("f"));

        typewriter("You see buildings...\n");
        typewriter("You hear something behind you...\n");
        System.out.println("Turn around[f]");

        do {
            ch = scanner.nextLine();
            if (ch.equals("f")) {
                break;
            } else
                System.out.println("Turn around[f]");
        } while (!ch.equals("f"));

        typewriter("You expected to see the forest...\n");
        wait(2000);
        typewriter("But instead you see a city...\n");
        wait(2000);
        typewriter("From the Industrial Revolution...\n");

        wait(3000);

        player("respawn.wav", 1f, false);

        wait(2000);
        gameover = true;
        good = true;
        return;
    }

    static void scenario7(Scanner scanner) {
        if (bgmusic != null) {
            bgmusic.stop();
        }
        player("hopeful.wav", 1f, true);

        int steps = 0;
        typewriter("You can see the light beyond it.\n");
        System.out.println("Walk towards it");

        while (steps != 5) {
            String input = scanner.nextLine();
            if (input.equals("w")) {
                player("footstep2.wav", -5f, false);
                steps++;
                y++;
            } else
                System.out.println("There's no turning back now, victory's up ahead");
        }
        typewriter("Civilization at last.\n");
        System.out.println("Walk towards it");

        steps = 0;
        while (steps != 5) {
            String input = scanner.nextLine();
            if (input.equals("w")) {
                player("footstep2.wav", -5f, false);
                steps++;
                y++;
            } else
                System.out.println("There's no turning back now, victory's up ahead");
        }
        steps = 0;

        typewriter("You can finally get out this god forsaken place.\n");
        System.out.println("Walk towards it");

        while (steps != 5) {
            String input = scanner.nextLine();
            if (input.equals("w")) {
                player("footstep2.wav", -5f, false);
                steps++;
                y++;
            } else
                System.out.println("There's no turning back now, victory's up ahead");
        }
        steps = 0;

        typewriter("Heck even the chapel smells bad.\n");
        System.out.println("Walk towards it");

        while (steps != 5) {
            String input = scanner.nextLine();
            if (input.equals("w")) {
                player("footstep2.wav", -5f, false);
                steps++;
                y++;
            } else
                System.out.println("There's no turning back now, victory's up ahead");
        }
        steps = 0;

        typewriter("Freedom at last.\n");
        System.out.println("Walk towards it");

        while (steps != 5) {
            String input = scanner.nextLine();
            if (input.equals("w")) {
                player("footstep2.wav", -5f, false);
                steps++;
                y++;
            } else
                System.out.println("There's no turning back now, victory's up ahead");
        }
        steps = 0;

        if (readdiary) {
            scenario8Good(scanner);
            return;
        } else {
            scenario8Bad(scanner);
            return;
        }

    }

    static void SecretEnding(Scanner scanner) { // * or just make him return by death to spawn 
        secret = true;

        if (bgmusic != null) {
            bgmusic.stop();
        }
        typewriter("You see something.\n");
        typewriter("And its not soil.\n");
        typewriter("A see of black. Its asphalt. It a road.");
        typewriter("You don't know where it leads.\n");
        typewriter("But you can see a small light in the distance.\n");
        typewriter("A street light, with a silhouette under it.\n");
        typewriter("The silhouette waves at you.\n");
        System.out.println("Walk towards it[w]");

        moveForward(scanner, 5, "Keep moving forward.", false, false, false);

        player("car.wav", 0f, true);
        typewriter("You hear a sound behind you.\n");
        typewriter("And its getting louder.\n");
        bgmusic.stop();
        player("car.wav", 3f, true);
        typewriter("Everything gets brighter, and brighter, and brighter.\n");
        typewriter("And the man waves harder.\n");

        System.out.println("Continue walking[w]");

        moveForward(scanner, 3, "Keep moving forward.", false, false, false);
        
        bgmusic.stop();
        player("car.wav", 6f, true);
        System.out.println(".....");
        player("horn.wav", 3f, false);
        player("crash.wav", 5f, false);
        bgmusic.stop();

        wait(4000);

        x = 0;
        y = 0;

        player("bg.wav", -5.0f, true);
        spawn(scanner);
        typewriter("Try walking forward with W\n");
        return;
    }

    static void GoBackToSpawn(Scanner scanner) {
        Random random = new Random();

        int hisx = 0, hisy = 0;
        String ch;
        bgmusic.stop();

        typewriter("You decided to go back to the car crash site.\n");
        System.out.println("Move forward[w]\n");

        while (y != 6) {
            ch = scanner.nextLine(); // 0,8 - 0,7
            if (ch.equals("w")) {
                player("footstep2.wav", -5f, false);
                System.out.println("Position: " + x + ", " + y--);
            } else
                System.out.println("Can't do that.");
        }

        typewriter("You reached the car crash site.\n");
        typewriter("You see a silhouette in the distance.\n");
        typewriter("But you were alone in this forest.\n");

        while (hisy != 3) {
            System.out.println("His position: " + hisx + ", " + hisy);
            hisy++;
            player("footstep2big.wav", -5f, false);
            wait(1000);
        }

        typewriter("RUN.\n");

        moveForwardWithHim(scanner, 2, "Not there.", hisx, hisy, false, 3f);
        hisy += 4;

        typewriter("You can see the chapel in the distance.\n");
        typewriter("But you're a little too slow.\n");

        wait(random.nextInt(3000));
        jumpscare();

        wait(3100);
        Game = false;
        deads = true;
        bad = true;
        return;
    }

    static void scene3(Scanner scanner) {
        typewriter("You decide to follow the path.");
        pathattempt = true;

        String[] lines = {
                "You walk.",
                "And walk.",
                "But the forest doesn't seem to change.",
                "You continued walking",
                "Theres footsteps behind you",
                "Its getting closer",
                "And closer",
                "And closer",
                "And closer",
                "And closer"
        };

        int move = 0;
        boolean goback = false;
        boolean scene = true;
        float vol = -10f;

        while (scene) { // The whole scene 3 lmao

            System.out.println("Walk [w]");

            String input = scanner.nextLine();
            if (input.equals("w")) { // scene 3 movement
                player("footstep2.wav", -5f, false);
                System.out.println("Position: " + x + ", " + y);
                x++;
                if (move >= 4) {

                    wait(500);
                    player("footstep2big.wav", vol, false);

                    vol += 2f;
                }
                if (move < lines.length)
                    typewriter(lines[move] + "\n");

                move++;
                visited = false;
            } else if (input.equals("s")) {
                if (move != 4) {

                    typewriter("You moved back.\n");
                    System.out.println("Position: " + x + ", " + y);
                    if (move == 1) {
                        x = 0;
                        y = 9;
                        chapel(scanner);
                        visited = false;
                    } else
                        x--;

                } else {
                    System.out.println("He's Behind You... Keep moving forward.");
                }

            } else if (input.equals("a")) {

                System.out.println("Cant go there");

            } else if (input.equals("d")) {

                System.out.println("Cant go there");

            } else

                System.out.println("W for up, A for left, S for down, D for right");

            if (move >= lines.length) { // if move is greater than lines, commence the stop and look back sequence
                System.out.print("Type stop: ");
                String ch;
                do {
                    ch = scanner.nextLine();
                    if (!ch.equals("stop"))
                        System.out.println("Type again.");
                } while (!ch.equals("stop"));

                ch = "";
                typewriter("You stopped.\n");
                typewriter(".....\n");
                typewriter("Nothing\n");

                System.out.print("Type [look back]: ");
                do {
                    ch = scanner.nextLine();
                    if (!ch.equals("look back"))
                        System.out.println("Type again.");
                } while (!ch.equals("look back"));

                ch = "";
                typewriter("You looked back.\n");
                typewriter(".....\n");
                typewriter("Nothing.\n");
                typewriter("You begin to wonder if you're imagining things.\n");
                System.out.print("Turn back[1] or Continue[2]: \n");

                do {
                    ch = scanner.nextLine();
                    if (ch.equals("1")) {

                        typewriter("You decided to go back\n");
                        goback = true;
                        scene = false;
                        break;
                    } else if (ch.equals("2")) {

                        typewriter("You continued walking.\n");
                        goback = false;
                        scene = false;
                        break;
                    }
                } while (!ch.equals("1") && !ch.equals("2"));
            }

            wait(350);

        }

        if (goback) {
            chapel(scanner);
            return;
        } else {
            scenario6(scanner);
            return;
        }
    }

    static void diary(Scanner scanner) {
        Random random = new Random();
        boolean audioplayed = false;

        String[] diary = {
                "I don't remember how I got here.",

                "I found a chapel.",

                "There was a road outside.",

                "I followed it.",

                "I ended up back at the chapel.",

                "page",

                "I tried again",

                "page",

                "I heard footsteps behind me",

                "page",

                "I didn't look",

                "page",

                "Then I looked",

                "page",

                "And I was-"
        };

        String ch;

        ch = "";
        typewriter("You turned to the next page\n");
        for (int i = 0; i < diary.length; i++) {

            if (diary[i].equals("page")) {
                System.out.println("Press 'd' for next page");
                do {
                    ch = scanner.nextLine();
                    if (!ch.equals("d"))
                        System.out.println("Press 'd' for next page");

                } while (!ch.equals("d"));

            } else
                typewriter(diary[i] + "\n");

            if (!audioplayed) {
                int event = random.nextInt(5);
                if (event == 3) {
                    player("footstepsmuffled.wav", 5f, false);
                    audioplayed = true;
                }
            }

            bgmusic.stop();
            wait(1000);

        }
        return;
    }

    static boolean chapel(Scanner scanner) { // scene 2
        String ch;
        chapel = true;

        if (pathattempt) { // the sequence if the player has already attempted the path and returned to the
                           // chapel
            typewriter("Will you go in or not?[y/y]: \n"); // just to mess with ya lol
            while (true) {
                ch = scanner.nextLine();
                if (ch.equals("y")) {
                    bgmusic.stop();
                    player("bgmuffled.wav", -5.0f, true);
                    break;
                } else
                    System.out.println("y/y");
            }

            typewriter("You decided to enter the chapel.\n");
            typewriter("You see the altar, the candle, and an old diary.\n");
            typewriter("There is no name written on it...\n");
            typewriter("Opening the diary, the first page says...\n");
            typewriter("I woke up alone in the forest, and it was dark...\n");
            typewriter("The hand writing is weirdly familiar\n");
            typewriter("Read it[1] or Keep it and leave[2] or leave it [3]?:\n");

            while (true) {
                ch = scanner.nextLine();
                if (ch.equals("1")) {
                    diary(scanner);
                    readdiary = true;
                    return true;

                } else if (ch.equals("2")) {
                    return true;

                } else if (ch.equals("3")) {
                    return false;
                } else
                    System.out.println("y/n");
            }

        } else {

            typewriter("You got to the chapel.\n");
            typewriter("Its in ruins, broken windows, and vines everywhere.\n");
            typewriter("Through the broken window, you noticed a lit candle.\n");
            typewriter(
                    "You also saw a pathway that goes straight east and might be the one the note is talking about.\n");
            typewriter("Will you go in or not?[y/d to go the the path]: \n");

            while (true) {
                ch = scanner.nextLine();
                if (ch.equals("y")) {

                    bgmusic.stop();
                    player("bgmuffled.wav", -5.0f, true);
                    break;
                } else if (ch.equals("d")) {
                    bgmusic.stop();

                    scene3(scanner);
                    return false;

                } else
                    System.out.println("y/d");
            }
        }
        typewriter("You decided to enter the chapel.\n");
        typewriter("You see the altar, the candle, and an old diary.\n");
        typewriter("There is no name written on it...\n");
        typewriter("Opening the diary, the first page says...\n");
        typewriter("I woke up in the forest, and it was dark...\n");
        typewriter("The hand writing is weirdly familiar\n");
        typewriter("Read it and leave[1] or Keep it and leave[2] or leave it[3]?:\n");

        while (true) {
            ch = scanner.nextLine();
            if (ch.equals("1")) {
                diary(scanner);
                readdiary = true;
                return true;

            } else if (ch.equals("2")) {
                return true;

            } else if (ch.equals("3")) {
                return false;
            } else
                System.out.println("y/n");
        }
    }

    static void YetAnotherSecretMethod(Scanner scanner){
        wait(1000);
        moveForward(scanner, 1, "Not there.", true, true, false);

        Objects object = objects.get(Integer.toString(x) + "," + Integer.toString(y));

        if (object != null) {
            if (object.locked) {
                System.out.println("Its locked");
            }

            if (object.visited) {
                System.out.println("You already visited " + object.name + ", move on");
            }

            System.out.println("You found: " + object.name);
            if (object.clues == true) {
                typewriter(object.clue);
                System.out.println("Continue walking forward");
            }
        }

        moveForward(scanner, 7, "Not there.", true, true, false);

        if (bgmusic != null) {
            bgmusic.stop();
        }
        wait(2000);
        typewriter("He's now behind you.");

        wait(2000);

        Game = false;
        gameover = true;
        secret = true;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        player("bg.wav", -5.0f, true);

        /* GAME LOOP */

        spawn(scanner);
        System.out.println("Try walking forward with W");

        while (Game) {

            /* MOVEMENT */

            String input = scanner.nextLine();
            if (input.equals("w")) {
                if (secret) {
                    wait(500);
                    player("footstep2big.wav", 2f, false);
                    YetAnotherSecretMethod(scanner);
                    break;
                }

                y++;
                visited = false;
            } else if (input.equals("s")) {
                if (secret){
                    System.out.println("Can't go there.");
                    continue;
            }               
                y--;
                visited = false;
            } else if (input.equals("a")) {
                if (secret){
                    System.out.println("Can't go there.");
                    continue;
            }
                x--;
                visited = false;
            } else if (input.equals("d")) {
                if (secret){
                    System.out.println("Can't go there.");
                    continue;
            }
                x++;
                visited = false;
            } else

                System.out.println("W for up, A for left, S for down, D for right");

            System.out.println("Position: " + Integer.toString(x) + ", " + Integer.toString(y));


            /* OBJECTS */

            Objects object = objects.get(Integer.toString(x) + "," + Integer.toString(y));

            if (object != null) {
                if (object.locked) {
                    System.out.println("Its locked");
                    continue;
                }

                if (object.visited) {
                    System.out.println("You already visited " + object.name + ", move on");
                    continue;
                }

                System.out.println("You found: " + object.name);
                if (object.clues == true) {
                    typewriter(object.clue);
                    System.out.println("Continue walking forward");
                }

                if (object.onetime == true) // remover
                    objects.remove(Integer.toString(x) + "," + Integer.toString(y));

                if (object.scene) {
                    if (object.name.equals("Chapel")) {
                        hasdiary = chapel(scanner);
                        Chapel.visited = true;

                        if (!scene6){
                            typewriter("You left the chapel.\n");
                            typewriter("You can see the path straight ahead.[1]\n");
                            typewriter("You can also see the car crash site.[2]\n");
                            typewriter("Where will you go? [1/2]\n");
                            String ch;
                            do {
                                ch = scanner.nextLine();
                                if (ch.equals("2")) {
                                    GoBackToSpawn(scanner);
                                    break;
                                } else if (ch.equals("1")) {
                                    break;
                                } else
                                    System.out.println("invalid");
                            } while (!ch.equals("1"));

                        }

                        if (hasdiary)
                            inventory.add("diary");

                        if (Game && !scene6) {
                            typewriter("You got out of the chapel and got on to the path.\n");
                            scenario6(scanner);
                        }

                        if (Game) {

                            scenario7(scanner);
                            break;
                        }
                    
                    }
                }

            }

            if (x == -20) {
                SecretEnding(scanner);
            }
        }

        /* Death and ending */
        if (deads) {
            player("static.wav", 2f, false);
            typewriter("You...\n");
            wait(1000);
            typewriter("Died.\n");
            wait(5000);

            // return to check point??
        } else if (gameover) {
            if (bad) {
                player("static.wav", 2f, false);
                typewriter("You died....\n");
                typewriter("You were too late.\n");
                wait(5000);
            } else if (good) {
                player("victory?.wav", 4f, false);
                typewriter("You survived.?\n");
                wait(5000);
            } else if (secret){
                player("callofwitch.wav", 1f, false);
                typewriter("......\n");
                wait(8000);
            }
        }

        System.out.println("Gameover" + (good ? " - Good Ending" : bad ? " - Bad Ending" : secret ? " - Secret Ending (It can all happen again.)" : ""));
    }                                    /* Prints if good ending *//* Print if bad ending *//* Print if secret ending */               
}