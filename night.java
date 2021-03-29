import java.util.Scanner;

public class night {
    Scanner scanner = new Scanner(System.in);
    public static int nightNumber = 0;
    public static Player MafiaKilled;
    public static Player mafiaTriedToKill;

    public night() {
        System.out.println("night" + (++nightNumber) + "\n");
        nightPlayers();
        nightVoting();
        game.EndGame();
    }

    //print players who should wake up at night
    public void nightPlayers() {
        for (int i = 0; i < game.getAlivePlayers().length; i++) {
            switch (game.getAlivePlayers()[i].getClass().getSimpleName()) {
                case "Joker":
                    break;
                case "villager":
                    break;
                case "detective":
                    System.out.println(game.getAlivePlayers()[i].getPlayerName() + ": " + game.getAlivePlayers()[i].getClass().getSimpleName() + "\n");
                    break;
                case "doctor":
                    System.out.println(game.getAlivePlayers()[i].getPlayerName() + ": " + game.getAlivePlayers()[i].getClass().getSimpleName() + "\n");
                    break;
                case "bulletproof":
                    break;
                case "mafia":
                    System.out.println(game.getAlivePlayers()[i].getPlayerName() + ": " + game.getAlivePlayers()[i].getClass().getSimpleName() + "\n");
                    break;
                case "godfather":
                    System.out.println(game.getAlivePlayers()[i].getPlayerName() + ": " + game.getAlivePlayers()[i].getClass().getSimpleName() + "\n");
                    break;
                case "silencer":
                    System.out.println(game.getAlivePlayers()[i].getPlayerName() + ": " + game.getAlivePlayers()[i].getClass().getSimpleName() + "\n");
            }
        }
    }

    public void nightVoting() {
        String inp = scanner.nextLine();
        while (!inp.equals("end_night")) {
            if (inp.equals("get_game_state")) {
                System.out.println("Mafia = " + game.getAliveMafia().length + "\n");
                System.out.println("Villager = " + game.getAliveVillagers().length + "\n");
                inp = scanner.nextLine();
                continue;
            }
            if (inp.equals("start_game")) {
                System.out.println("game has already started\n");
                inp = scanner.nextLine();
                continue;
            }
            String[] input = inp.split(" ");
            if (input.length < 2) {
                System.out.println("user not found\n");
                inp = scanner.nextLine();
                continue;
            }
            Player a = game.FindPlayer(input[0]);
            Player b = game.FindPlayer(input[1]);

            if (a == null | b == null) {
                System.out.println("user not joined\n");
                inp = scanner.nextLine();
                continue;
            }

            if (!a.isAlive) {
                System.out.println("user is dead\n");
                inp = scanner.nextLine();
                continue;
            } else {
                switch (a.getClass().getSimpleName()) {
                    case "Joker":
                        System.out.println("user can not wake up during night\n");
                        break;
                    case "villager":
                        System.out.println("user can not wake up during night\n");
                        break;
                    case "detective":
                        if (!b.getIsAlive()) {
                            System.out.println("suspect is dead\n");
                        } else if (detective.AlreadyAsk) {
                            System.out.println("detective has already asked\n");
                        } else {
                            detective.isMafia(b);
                            detective.AlreadyAsk = true;
                        }
                        break;
                    case "doctor":
                        if (checkB(b))
                            doctor.SavedByDoctor = b;
                        break;
                    case "bulletproof":
                        System.out.println("user can not wake up during night\n");
                        break;
                    case "mafia":
                        if (checkB(b))
                            a.setNightVote(b);
                        break;
                    case "godfather":
                        if (checkB(b))
                            a.setNightVote(b);
                        break;
                    case "silencer":
                        if (silencer.isCalledBefore) {
                            if (checkB(b))
                                a.setNightVote(b);
                        } else {
                            if (checkB(b)) {
                                silencer.setCalledBefore(true);
                                silencer.setSilence(b);
                                silencer.silenced = b;
                            }
                        }
                        break;
                    default: {
                        System.out.println("role not found\n");

                    }
                }
            }
            inp = scanner.nextLine();
        }


        for (int i = 0; i < game.getAliveMafia().length; i++) {
            if (game.getAliveMafia()[i].getNightVote() != null)
                game.getAliveMafia()[i].getNightVote().setVote();
        }
        int vote = 0;
        int repeat = 0;
        Player a = null;
        for (int j = 0; j < game.getAlivePlayers().length; j++) {
            if (game.getAlivePlayers()[j].getVote() > vote) {
                vote = game.getAlivePlayers()[j].getVote();
            }
        }

        Player[] equalvote = new Player[game.getAlivePlayers().length];
        int n = 0;
        for (int k = 0; k < game.getAlivePlayers().length; k++) {
            if (game.getAlivePlayers()[k].getVote() == vote) {
                equalvote[n] = game.getAlivePlayers()[k];
                repeat++;
                n++;

            }
        }
        if (repeat == 2) {
            if (equalvote[0].equals(doctor.SavedByDoctor))
                MafiaKilled = equalvote[1];
            else if (equalvote[1].equals(doctor.SavedByDoctor))
                MafiaKilled = equalvote[1];
        }
        if (repeat == 1) {
            mafiaTriedToKill = equalvote[0];
            if (!mafiaTriedToKill.equals(doctor.SavedByDoctor)) {
                MafiaKilled = mafiaTriedToKill;
                MafiaKilled.setAlive(false);
            }
        }
        for (int k = 0; k < game.getAlivePlayers().length; k++)
            game.getAlivePlayers()[k].resetVote();
        if (MafiaKilled != null) {
            if (MafiaKilled.getClass().getSimpleName().equals("bulletproof")) {
                if (!bulletproof.AlreadyKilled) {
                    bulletproof.AlreadyKilled = true;
                    MafiaKilled.setAlive(true);
                    MafiaKilled.resetVote();
                    MafiaKilled = null;
                    mafiaTriedToKill = null;
                }
            }
        }
        doctor.SavedByDoctor = null;
        silencer.isCalledBefore = false;
        detective.AlreadyAsk=false;
    }

    public boolean checkB(Player b) {
        if (!b.getIsAlive()) {
            System.out.println("votee already dead");
            return false;
        } else return true;
    }
}
