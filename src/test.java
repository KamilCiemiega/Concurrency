import java.util.Random;

class MessageRepository {
    private String message;
    private boolean hasMessage = false;

    public synchronized String read(){

        while (!hasMessage){
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        hasMessage = false;
        notifyAll();
        return message;
    }

    public synchronized void write(String message){
        while (hasMessage) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        hasMessage = true;
        notifyAll();
        this.message = message;
    }
}

class MessageWriter implements Runnable {
    private MessageRepository outGoingMessage;
    private final String text = "Some text";

    public MessageWriter(MessageRepository outGoingMessage){
        this.outGoingMessage = outGoingMessage;
    }

    @Override
    public void run() {
        Random random = new Random();
        String[] lines = text.split("\n");

        for (int i = 0; i < lines.length; i++){
            outGoingMessage.write(lines[i]);
            try {
                Thread.sleep(random.nextInt(500, 2000));
            }catch (InterruptedException e){
                throw  new RuntimeException(e);
            }
        }
        outGoingMessage.write("Finished");
    }
}

class MessageReader implements Runnable{
    private MessageRepository inGoingMessage;

    public MessageReader(MessageRepository inGoingMessage) {
        this.inGoingMessage = inGoingMessage;
    }

    @Override
    public void run() {
        Random random = new Random();
        String latestMessage = "";

        do {
            try {
                Thread.sleep(random.nextInt(500, 2000));
            }catch (InterruptedException e){
                throw  new RuntimeException(e);
            }
        } while (!latestMessage.equals("Finished"));
    }
}

//public class Main {
//    public static void main(String[] args) {
////        MessageRepository messageRepository = new MessageRepository();
////        Thread reader = new Thread(new MessageReader(messageRepository));
////        Thread writer = new Thread(new MessageWriter(messageRepository));
////        reader.start();
////        writer.start();
//
//        System.out.println("test");
//    }
//}
