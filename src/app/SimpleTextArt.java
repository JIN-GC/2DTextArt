package app;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;
import java.util.Scanner;

import javax.imageio.ImageIO;


// ref https://eng-entrance.com/java-image

public class SimpleTextArt {
	//	テキスト表現文字用の変数宣言
	private static final String ASCII_CHARS = "@%#*+=-:. ";
	//	private static final String ASCII_CHARS = "@B%8WM#*oahkbdpwmZO0QCJYXzcvnxrjft/\\|()1{}[]-_+~<>i!lI;:,\"^`\'. ";
	//	Scanner型インスタンス生成
	private static Scanner sc = new Scanner(System.in);
	//	PrintStream型のオブジェクト初期値設定（メソッドの直接利用が可能）
	private static PrintStream so = System.out;
	//	PrintStream は、ファイルやネットワークなどへの出力にも利用が可能だが、リダイレクトやログ出力などの
	//	機能により出力先が変更されることに注意。また、他者の可読性が低いため利用は推奨されていないようです。
	//	PrintWriter は、高度なエラーハンドリングを行う場合に有用でcheckErrorメソッドでの確認が可能

	public static void main(String[] args) {


		try {
			//	入力値取得
			String input = null;
			//	ファイル読み込み
			BufferedImage image = null;

			try {
				while(true){
					so.print("読込ファイルの\033[1m URL \033[0mを入力してください。\n"
							+"[MENU] ローカルパス入力:\033[34m\033[1m z \033[0m "
							+"終了:\033[34m\033[1m q \033[0m \n\033[1m>>");
					//	終了処理
					input = sc.nextLine();
					if(input.matches("q")) System.exit(0);
					if(!input.isBlank() || !input.isEmpty() || input != null || !input.matches("z") || !input.matches("q")) image = checkAccess.isUrl(input);

					if(input.isBlank() || input.isEmpty() || input == null || input.matches("z")) input = getFilePath.geFileList(getFilePath.getWinDesktopPath());
					if(input.matches("q")) System.exit(0);

					if(input.isBlank() || input.isEmpty() || input == null || input.matches("z")) input = getFilePath.geFileList("src/app/storage/in");
					if(!input.isBlank() || !input.isEmpty() || input != null || !input.matches("z") || !input.matches("q")) image = checkAccess.isPath(input);

					if(input.matches("q")) {
						so.print("今回は終了します。");
						System.exit(0);
					} else if (!input.matches("z")) {
					} break; 
				}
			} catch (Exception e) {
				e.printStackTrace();
				so.println();
			}

			//	画像サイズ調整用の基準値（横幅）
			int width = 100;
			//	画像サイズ調整用の基準値（縦幅）
			int height = (int) ((double) image.getHeight() * width / image.getWidth());
			// 画像サイズの拡大縮小調整
			BufferedImage resizedImage = resizeImage(image, width, height);

			// テキストアートの生成
			String textArt = convertToTextArt(resizedImage);
			System.out.println(textArt);

			// エクスポートオプションの確認
			System.out.print("画像をエクスポートしますか？ (y/n): ");
			String exportOption = sc.nextLine().trim().toLowerCase();

			if (exportOption.equals("y")) {
				// エクスポートする形式を選択
				System.out.print("エクスポート形式を選択してください (1:txt/2:png): ");
				String exportFormat = sc.nextLine().trim().toLowerCase();

				if (exportFormat.equals("1")) {
					// テキストファイルとしてエクスポート
					System.out.print("保存するテキストファイルのパスを入力してください: ");
					String filePath = sc.nextLine().trim();
					if(filePath.isEmpty()) filePath = "%HOMEPATH%\\Desktop\\TextArt.txt" ;
					exportAsTextFile(textArt, filePath);
					System.out.println("テキストファイルとしてエクスポートしました。");
				} else if (exportFormat.equals("2")) {
					// PNGファイルとしてエクスポート
					System.out.print("保存するPNGファイルのパスを入力してください: ");
					String filePath = sc.nextLine().trim();
					if(filePath.isEmpty()) filePath = "%HOMEPATH%\\Desktop\\TextArt.png" ;
					exportAsPngImage(resizedImage, filePath);
					System.out.println("PNGファイルとしてエクスポートしました。");
				} else {
					System.out.println("無効な形式です。エクスポートをキャンセルします。");
				}
			} else {
				System.out.println("エクスポートをキャンセルしました。");
			}
		} catch (Exception e) {
			System.out.println("画像の読み込みに失敗しました。正しいURLまたはファイルパスを入力してください。");
		}
	}




	private static BufferedImage loadFile() throws IOException {

		//	BufferedImageクラスの変数を宣言
		BufferedImage image;
		//	読み込まれた画像編集するために　JavaのSwing（java.awt.image.BufferedImage）を利用
		//	SwingのBufferedImageクラスオブジェクトに対し変更を加えます。
		//	例)	読み込んだ画像を画面に表示することができる。
		//	BufferedImageを利用するためにImageIOクラス（javax.imageio.ImageIO）を利用。
		//	例)	ファイルからの画像読込、画像のファイル書き出しを行うことができる。

		// ユーザーにURLまたはファイルパスの入力を求める
		System.out.println("画像のURLまたはファイルパスを入力してください: ");
		String input = sc.nextLine();

		//	デフォルトのURL
		//	    	return "src/app/storage/in/no-files.jpg";

		//	String file = "https://marketplace.canva.com/EAFQH3NWe80/1/0/1600w/canva-%E7%99%BD-%E3%82%A4%E3%83%B3%E3%83%91%E3%82%AF%E3%83%88-%E7%8C%AB-%E5%86%99%E7%9C%9F-instagram%E3%81%AE%E6%8A%95%E7%A8%BF-Q5t4nTJMgoA.jpg";
		String file = "https://www.kawabe.co.jp/_wp/wp-content/uploads/%E3%83%89%E3%83%A9%E3%81%88%E3%82%82%E3%82%93%E7%B4%B9%E4%BB%8B-002.jpg";
		//	File file = new File("C:/sample.txt");
		//        Path file = Paths.get("https://www.kawabe.co.jp/_wp/wp-content/uploads/%E3%83%89%E3%83%A9%E3%81%88%E3%82%82%E3%82%93%E7%B4%B9%E4%BB%8B-002.jpg");

		//        if (!input.isEmpty()) file = Paths.get(input);
		if (!input.isEmpty()) file = input;

		//	@TODO 改修
		if (file.startsWith("http")){
			//        if (Files.notExists(file)){		//	if (Files.notExists(file) || file.startsWith("http")){
			//	URLから画像を読み込む
			image = ImageIO.read(new URL(file.toString()));
		} else {
			// ファイルパスから画像を読み込む
			image = ImageIO.read(new File(file.toString()));
		}
		return image; 
	}




	private static BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
		BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics2D = resizedImage.createGraphics();
		graphics2D.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
		graphics2D.dispose();
		return resizedImage;
	}

	private static String convertToTextArt(BufferedImage image) {
		StringBuilder textArt = new StringBuilder();
		int width = image.getWidth();
		int height = image.getHeight();
		Font font = new Font("Monospaced", Font.BOLD, 3);
		FontMetrics fontMetrics = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB).getGraphics().getFontMetrics(font);
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				Color pixelColor = new Color(image.getRGB(x, y));
				int grayScale = (pixelColor.getRed() + pixelColor.getGreen() + pixelColor.getBlue()) / 3;
				int index = (grayScale * (ASCII_CHARS.length() - 1)) / 255;

				char asciiChar = ASCII_CHARS.charAt(index);
				textArt.append(asciiChar).append(asciiChar);
			}
			textArt.append("\n");
		}
		return textArt.toString();
	}

	private static void exportAsTextFile(String text, String filePath) throws IOException {
		File file = new File(filePath);
		file.createNewFile();
		java.io.FileWriter writer = new java.io.FileWriter(file);
		writer.write(text);
		writer.close();
	}

	private static void exportAsPngImage(BufferedImage image, String filePath) throws IOException {
		ImageIO.write(image, "png", new File(filePath));
	}
}
