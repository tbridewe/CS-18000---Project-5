//public void login() {
//        ObjectInputStream ois = null;
//        try {
//            ois = new ObjectInputStream(new FileInputStream("file.txt"));
//            Object obj;
//            // String userEmail = /GUI textfield that has email/
//            // String userPassword = /GUI textfield that has password/
//
//            while ((obj = ois.readObject()) != null) {
//                if (obj instanceof Customer && ((Customer) obj).getEmail().equals(userEmail) && ((Customer) ((Customer) obj).getPassword().equals(userPassword))) {
//                    Customer customer = customer;
//                    break;
//                }
//                if (obj instanceof Seller && ((Seller) obj).getEmail().equals(userEmail) && ((Seller) obj).getPassword().equals(userPassword)) {
//                    Seller seller = seller;
//                    break;
//                }
//            }
//            ois.close();
//        } catch (EOFException eofException) {
//            // display GUI that says user not found, create an account
//        } catch (IOException | ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//
//
