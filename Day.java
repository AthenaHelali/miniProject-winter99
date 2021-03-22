import java.util.Scanner;

public class Day {
    Scanner scanner = new Scanner(System.in);
    public static int DayNumber = 0;

    public Day() {
        System.out.println("Day" + (++DayNumber) + "\n");
        this.voting();
    }

    public boolean checkVotee(String name) {
        Player temp = game.FindPlayer(name);
        if (temp == null) {
            System.out.println("user not found\n");
            return false;
        }
        if (temp.getIsSilence()) {
            System.out.println("voter is silenced\n");
            return false;
        }
        return true;
    }

    public void voting() {
        while (!scanner.nextLine().equals("end_vote")) {
            String[] temp = scanner.nextLine().split(" ");
            if (checkVotee(temp[0])) {
                Player a = game.FindPlayer(temp[1]);
                if (a == null) {
                    System.out.println("user not found\n");
                    continue;
                } else if (!a.getIsAlive()) {
                    System.out.println("votee already dead\n");
                    continue;
                } else {
                    a.setVote();
                }
            }
        }

        String mostVotedPlayer = null;
        int vote = 0;
        int repeat = 0;
        for (int j = 0; j < game.countAlivePlayers(); j++) {
            if (game.getAlivePlayers()[j].getVote() > vote) {
                mostVotedPlayer = game.getAlivePlayers()[j].getPlayerName();
                vote = game.getAlivePlayers()[j].getVote();
            }

        }
        for (int k = 0; k < game.countAlivePlayers(); k++) {
            if (game.getAlivePlayers()[k].getVote() == vote)
                repeat++;

        }
        if (repeat < 2) {
            if (game.FindPlayer(mostVotedPlayer).getClass().getSimpleName().equals("Joker")) {
                System.out.println("Joker won!\n");
                game.gameEnd = true;
            } else {
                game.FindPlayer(mostVotedPlayer).setAlive(false);
                System.out.println(game.FindPlayer(mostVotedPlayer).getPlayerName() + "died\n");
            }
        } else {
            System.out.println("nobody died\n");
        }
        for (int i=0;i<game.getAlivePlayers().length;i++){
            game.getAlivePlayers()[i].resetVote();

        }
    }

}
