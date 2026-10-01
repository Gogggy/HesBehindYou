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
    public static boolean deads = true;
    public static boolean readdiary = false;
    public static boolean scene6;
    public static boolean chapel = false;
    public static boolean gameover = false;
    public static boolean bad = false;
    public static boolean good = true;
    private static final Random RANDOM = new Random();

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

            try {

                if (i + 1 < input.length() && input.charAt(i) == ',') {
                    Thread.sleep(250);
                } else if (i + 1 < input.length() && input.charAt(i) == '.') {
                    Thread.sleep(300);
                } else
                    Thread.sleep(50);

            } catch (Exception e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    static void spawn(Scanner scanner) {
        typewriter("You woke up in a dark forest.\n");
        typewriter("You remember nothing.\n");
        typewriter("Your head hurts like hell, not even comparable to a hammer being struck in it.\n");
        typewriter("Despite that you pulled yourself together. You got up.\n");
    }

    static void moveForward(Scanner scanner, int stepCount, String message) { // boolean footstepsbehind
        int steps = 0;
        while (steps != stepCount) {
            String input = scanner.nextLine();
            if (input.equals("w")) {
                player("footstep2.wav", -5f, false);
                steps++;
                y++;
            } else // if footstepsbehind {wait 500 then footstep2big.wav}
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

        moveForward(scanner, 5, "There's no turning back now, the road is up ahead.");

        typewriter("The forest becomes weirdly familiar\n");

        typewriter("Continue walking[w]\n");

        moveForward(scanner, 3, "There's no turning back now, the road is up ahead.");

        typewriter("That was the same rock\n");

        moveForward(scanner, 3, "There's no turning back now, the road is up ahead.");

        typewriter("That was the same clearing\n");

        moveForward(scanner, 3, "There's no turning back now, the road is up ahead.");

        typewriter("You remember seeing them all\n");

        moveForward(scanner, 3, "There's no turning back now, the road is up ahead.");

        typewriter("But you don't remember when\n");

        float vol = -10f;
        player("suspense.wav", 2f, true);

        typewriter("Continue walking[w]\n");

        int hisx = x, hisy = y - 14;

        moveForwardWithHim(scanner, 3, "There's no turning back now, the road is up ahead.", hisx, hisy, false, vol++);

        hisy += 6;
        typewriter("That was not your footstep.\n");
        System.out.println("Press 'f' to look back\n");

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
        typewriter("He's right behind you.");
        wait(5000);
        bgmusic.stop();
        typewriter("Silence engulfed the forest\n");
        typewriter("You hear your own breath\n");
        player("breathing.wav", 2f, true);
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
        Random random = RANDOM;
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

                "And I was wrong",

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

            wait(1000);

        }
        return;

    }

    static void chapel2(Scanner scanner) {
        Random random = RANDOM;
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
        Random random = RANDOM;
        boolean scene = true;
        bgmusic.stop();

        int hisx = 0, hisy = 0;

        typewriter("But then... ");
        wait(2000);
        typewriter("The world goes silent...\n");
        wait(3000);

        String[] lines = {
                "The chapel is right in front of you....\n",
                "What... the... hell... just... happened...\n",
                "You were almost there.\n",
                "You can see the light.\n",
                "You can almost smell the pollution.\n",
                "Why are you back here?\n",
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
                typewriter("Why am I back?");
                System.out.println("Keep moving forward[w]\n");
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

        bgmusic.stop();
        typewriter("He's....");
        wait(1000);
        typewriter("Behind you now....\n");
        wait(1000);
        typewriter("Be.. quiet...\n");
        wait(1000);
        typewriter("Dont.. Move...\n");

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
        wait(random.nextInt(2000));

        jumpscare();

        wait(3100);
        Game = false;
        gameover = true;
        bad = true;
        return;
        // make it so that it ends and returns the bad ending

        // choice (inside this module or maybe chapel2()) to enter it again but this
        // time, only read the diary
        // If he read the diary its still too late, he'll still die.
        // Then only choice is the path
        // he sees the path
        // tries to run
        // keeps returning (every returns play the rezero sound)
        // this is for like 2 tries

        // something like hes behind you now then he cant move
    }

    static void scenario8Good() {
        System.out.println("wow good ending grape");
        Game = false;
        return;
    }

    static void scenario7(Scanner scanner) {
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
            scenario8Good();
            return;
        } else {
            scenario8Bad(scanner);
            return;
        }

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

        while (scene) { //The whole scene 3 lmao

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
        Random random = RANDOM;
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

                "And I was wrong"
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
    }

    static boolean chapel(Scanner scanner) {
        String ch;
        chapel = true;

        if (pathattempt) { //the sequence if the player has already attempted the path and returned to the chapel
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

                }else 
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

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        player("bg.wav", -5.0f, true);

        // OBJECTS //

        HashMap<String, Objects> objects = new HashMap<>();

        Objects note = new Objects("Note",
                "There was once a chapel straight ahead of here. It has a cemetery beside it.\n" +
                        "It can be accessed from the road outside. It was a place of peace — a place of serenity.\n" +
                        "Find the road. From there, you might be able to reach the nearest town.\n",
                true, unlock, !interactable, false, true, visited, false);
        objects.put("0,1", note);

        Objects chapel = new Objects("chapel", "", false, unlock, interactable, false, false, visited, true);
        objects.put("0,9", chapel);

        // GAME LOOP //

        spawn(scanner);
        System.out.println("Try walking forward with W");

        while (Game) {

            // MOVEMENT //

            String input = scanner.nextLine();
            if (input.equals("w")) {

                y++;
                visited = false;
            } else if (input.equals("s")) {

                y--;
                visited = false;
            } else if (input.equals("a")) {

                x--;
                visited = false;
            } else if (input.equals("d")) {

                x++;
                visited = false;
            } else

                System.out.println("W for up, A for left, S for down, D for right");

            System.out.println("Position: " + Integer.toString(x) + ", " + Integer.toString(y));

            // OBJECTS //

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
                    if (object.name.equals("chapel")) {
                        hasdiary = chapel(scanner);
                        chapel.visited = true;
                        if (hasdiary)
                            inventory.add("diary");

                        if (Game && !scene6)
                            typewriter("You got out of the chapel and got on to the path.\n"); //maybe a choice to go back to his previous place then he dies there.
                            scenario6(scanner);

                        if (Game)
                            scenario7(scanner);
                    }

                } // if object null

            }

        }
        System.out.println("Haha");
    }
}