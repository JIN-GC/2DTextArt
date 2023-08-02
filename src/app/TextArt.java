package app;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.imageio.ImageIO;

import tool.CheckAccess;
import tool.GeFileList;
import tool.GetFilePath;


// ref https://eng-entrance.com/java-image
//	
public class TextArt {
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

		//	入力値取得
		String input = null;
		//	ファイル読込み
		BufferedImage image = null;

		try {

			try {
				while(true){
					so.print("\033[1m[MENU] 読込ファイルの\033[34m URL \033[0m\033[1mを入力（http/https～のみ）\033[0m\n"
							+"ローカルパス参照:\033[34m\033[1m z \033[0m "
							+"終了:\033[34m\033[1m q \033[0m \n\033[1m>>");
					input = sc.nextLine();

					if ((!input.isBlank() || input != null || !input.matches("z") || !input.matches("q"))&& input.startsWith("http")) {
						//	if ((!input.isBlank() || input != null || !input.matches("z") || !input.matches("q"))&& input.trim().toLowerCase().startsWith("http")) {
						image = CheckAccess.isUrl(input);
						break;
					} else if (input.matches("q")) {
						//	終了処理
						so.println("[処理終了]");
						System.exit(0);
					}

					input = GeFileList.getList(GetFilePath.getWinDesktop());
					if ((!input.isBlank() || input != null) && !input.matches("z")) {
						image = CheckAccess.isPath(input);
						if (image == null) {
							so.println("\033[1m\033[41m[画像読込異常検知] 画像および入力情報をご確認後にリトライください。\033[0m\n");
							so.println("[処理終了]");
							System.exit(0);
						} else {
							break;
						}
					}
					// if (input.matches("z")) 次へ

					input = GeFileList.getList("src\\storage\\in\\");
					if ((!input.isBlank() || input != null) && !input.matches("z")) {
						image = CheckAccess.isPath(input);
						if (image == null) {
							so.println("\033[1m\033[41m[画像読込異常検知] 画像および入力情報をご確認後にリトライください。\033[0m\n");
							so.println("[処理終了]");
							System.exit(1);
						}
						break;
					}
					// if (input.matches("z")) 繰返し
					so.println("[繰返し処理実行]\n");
				}
			} catch (Exception e) {
				e.printStackTrace();
				so.println("\033[1m\033[41m[画像読込異常検知] 画像および入力情報をご確認後にリトライください。\033[0m\n");
				so.println("[処理終了]");
				System.exit(1);
			}

			//	画像サイズ調整用の基準値（横幅）
			so.print("\n\033[1m[出力横幅サイズ（文字数）入力] ※推奨値：80～100\n\033[5m>>");
			input = Integer.toString(sc.nextInt());
			int width = 80;
			if(!input.isEmpty() || input != null) width = Integer.parseInt(input);
			//	画像サイズ調整用の基準値（縦幅）
			int height = (int) ((double) image.getHeight() * width / image.getWidth());
			// 画像サイズの拡大縮小調整
			BufferedImage resizedImage = resizeImage(image, width, height);

			// テキストアートの生成
			//	so.print("\033[1m[エクスポートタイプ] 0: カラー  1: モノクロ（デフォルト）\n\033[5m>>");
			//	int color = sc.nextInt();
			String textArt = convertToTextArt(resizedImage, 1);
			so.println(textArt);

			// エクスポート確認
			so.print("\033[1m[エクスポートの有無] YES: y  NO: n （デフォルト）\n\033[5m>>");
			sc.nextLine();
			String expOptQ = sc.nextLine().trim().toLowerCase();
			if (expOptQ.equals("y")) {

				// エクスポート形式選択
				so.print("\033[1m[エクスポートタイプ] 0: png  1: txt（デフォルト）\n\033[5m>>");
				String ext = sc.nextLine().trim();
				if (ext.equals("0")) {
					ext = ".png";
				} else {
					ext = ".txt";
				}

				so.print("\033[1m[フォルダパス] 0: WINDOWSデスクトップ  1: ストレージ（デフォルト） \n\033[5m>>");
				String dirPath = sc.nextLine().trim();
				if (dirPath.equals("0")) {
					dirPath = GetFilePath.getWinDesktop();
				} else {
					dirPath = "src\\storage\\out\\";
				}
				String filePath = dirPath + "TextArt" + ext;
				if (ext.equals(".png")) {
					// TODO 2DART
					exportAsPngImage(filePath, ext, textArt, width, height);
				} else {
					exportAsTextFile(filePath, ext, textArt);
				}
				so.printf("[エクスポート完了] \033[1m\033[34m%s\033[0m\n", filePath);
				so.println("[処理終了]");
			} else {
				so.println("[処理終了]");
			}
		} catch (Exception e) {
			so.println("\033[1m\033[31m[画像読込異常検知] 画像および入力情報をご確認後にリトライください。");
			so.println("[処理終了]");
		}
		so.close();
		sc.close();
	}
	//
	//		//	@TODO 改修
	//		if (file.startsWith("http")){
	//			//        if (Files.notExists(file)){		//	if (Files.notExists(file) || file.startsWith("http")){
	//			//	URLから画像を読み込む
	//			image = ImageIO.read(new URL(file.toString()));
	//		} else {
	//			// ファイルパスから画像を読み込む
	//			image = ImageIO.read(new File(file.toString()));
	//		}
	//


	private static BufferedImage resizeImage(BufferedImage img, int width, int height) {
		//	BufferedImage は java.awt.imageパッケージにあるImageを継承したサブクラス[参照*1]
		//	このクラスフィールドを使用して、仮引数に縦幅、横幅、イメージのタイプ(カラーモデルと呼ばれるものの種類)を設定
		//	TYPE_INT_RGB: 8 ビット RGB 色成分によるイメージ（一般的）
so.println("resizeImage  w:h " + width + ":" + height);
		BufferedImage resizeImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		//	BufferedImage に描画するために使用できる　Graphics2D を設定
		Graphics2D g2D = resizeImg.createGraphics();
		//	Graphics2Dクラスは、イメージを描画するための基本クラスです。
		//	drawImage(Imageオブジェクト, x座標, y座標, 監視コンポーネント)と指定します。[参照*2]
		//	監視コンポーネントの引数: 指定しておくと画像を監視し、必要な時に再描画命令を出してくれます??。
		//	obs - Imageのより多くの部分が変換されると通知されるImageObserver。
		//	TODO 説明を短縮＆要約
		g2D.drawImage(img, 0, 0, width, height, null);
		//	Graphics メモリ使用領域解放
		g2D.dispose();
		return resizeImg;
	}

	private static String convertToTextArt(BufferedImage image, int color) {
		//	StringBuilder は可変長型で複数の文字列を連結・編集する為のインスタンスを生成
		StringBuilder textArt = new StringBuilder();
		StringBuilder textArtC = new StringBuilder();
		//	image より、横・縦幅取得
		int width = image.getWidth(), height = image.getHeight();

		/*
		//	Font(String フォント名, int フォント型, int フォントサイズ)を生成
		//	Font font = new Font("Monospaced", Font.BOLD, 3);
		//	フォント名: Serif/SansSerif/Dialog/DialogInput/Monospacedなど
		//	フォント型: PLAIN/BOLD/ITALIC
		//	フォントサイズ: ポイント指定（1ポイント=4/3ピクセル/1ピクセル=3/4ポイント　）

		//	FontMetrics オブジェクトに指定フォント情報を含めインスタンスを生成
		//	フォント情報取得用の小さな1ピクセルサイズのキャンパスをBufferedImageにより1x1ピクセルのRGBイメージで作成.
		//	getGraphics()メソッドにより、取得したGraphicsコンテキスト（Graphicsオブジェクト）に対して、指定したフォント（fontで設定した文字の幅、高さなどの情報）を設定。
		//	FontMetrics fontMetrics = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB).getGraphics().getFontMetrics(font);
		//	FontMetricsクラスは、特定の画面上での特定のフォントの描画に関する情報をカプセル化するフォント・メトリックス・オブジェクトを定義します。
		//	https://sun.ac.jp/prof/yamagu/java8docs/api/java/awt/FontMetrics.html
		//	テキストを描画する際など、思い通りの位置に描画するためにはFontMetricsを使用して座標を計算します
		//	https://www2.denshi.numazu-ct.ac.jp/staff/java/tutorial/jtutorial/ui/drawing/drawingText.html
		//	FontMetricsを取得(GraphicsEnvironment版)　BufferedImageより、直接GraphicsEnvironmentのほうが精度有効？。
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		Graphics graphics = ge.getDefaultScreenDevice().getDefaultConfiguration().createCompatibleImage(1, 1).getGraphics();
		FontMetrics fontMetrics = graphics.getFontMetrics(font);
		//	フォント情報取得テスト用
		int fontHeight = fontMetrics.getHeight();
		int stringWidth = fontMetrics.stringWidth("Hello, World!");
		so.println("Font Height: " + fontHeight);
		so.println("String Width: " + stringWidth);
		 */

		// １ピクセル毎にカラー情報を文字へ変換
		// 画像の縦幅分だけ繰り返す(y: 0からheight未満まで)
		for (int y = 0; y < height; y++) {
			// 画像の横幅分だけ繰り返す (x: 0からwidth未満まで)
			for (int x = 0; x < width; x++) {
				// 1ピクセル毎に16進数で指定された座標のカラーコードを取得
				Color pixelColor = new Color(image.getRGB(x, y));
				//	カラー画像の各ピクセルのRGB値を平均化してグレーのカラーコードに置換え変換
				//	グレースケール変換は、カラーピクセルのRGB値（赤、緑、青の成分）を平均化してグレーのカラーコードに変換します。
				//	通常はRGB値の合計を3で割る方法で数学的にはRGB値の平均を計算していることになるそうです。
				int grayScale = (pixelColor.getRed() + pixelColor.getGreen() + pixelColor.getBlue()) / 3;
				// カラーコードを文字に変換
				int index = (grayScale * (ASCII_CHARS.length() - 1)) / 255;
				char asciiChar = ASCII_CHARS.charAt(index);
				//	変換文字をテキスト配列に追加
				textArt.append(asciiChar).append(asciiChar);

				//	カラー文字の取得
				int indexC = ((pixelColor.getRed() + pixelColor.getGreen() + pixelColor.getBlue())) % ASCII_CHARS.length();
				char asciiCharC = ASCII_CHARS.charAt(indexC);
				//	char asciiCharC = getColoredASCIIChar(pixelColor, ASCII_CHARS);
				//	変換文字をテキスト配列に追加
				textArtC.append(asciiCharC).append(asciiCharC);
			}
			// １行終了時に改行追加
			textArt.append("\n");
			textArtC.append("\n");
		}
		if(color == 0) return textArtC.toString();
		return textArt.toString();
	}


	private static void exportAsTextFile(String filePath, String ext, String textArt) throws IOException {

		File file = new File(filePath);
		// ファイル名重複時、ファイル名変更
		int counter = 1;
		while (file.exists()) {
			filePath = "output" + counter + ext;
			file = new File(filePath);
			counter++;
		}
		file.createNewFile();
		FileWriter writer = new FileWriter(file);
		writer.write(textArt);
		writer.close();
	}

	private static void exportAsPngImage(String filePath, String ext, String textArt, int width, int height ) throws IOException {
		// 背景色設定
		Color backgroundColor = Color.WHITE;
		// テキスト色設定
		Color textColor = Color.BLUE;
		// フォント設定
		Font font = new Font("Monospaced", Font.PLAIN, 1);
		//	フォント名: Serif/SansSerif/Dialog/DialogInput/Monospacedなど
		//	フォント型: PLAIN/BOLD/ITALIC
		//	フォントサイズ: ポイント指定（1ポイント=4/3ピクセル/1ピクセル=3/4ポイント　）

		//	出力用配列
		List<String> txtAry = Stream.of(textArt.split("\n")).collect(Collectors.toList());
		
		//	List<String> txtAry = (Arrays.asList(textArt.split("\n")));
		//	https://www.techiedelight.com/ja/convert-comma-separated-string-to-list-java/

		// 画像生成
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = image.createGraphics();
		g.setColor(backgroundColor);
		g.fillRect(0, 0, width, height);
		g.setColor(textColor);
		g.setFont(font);
		for (int y = 0; y < txtAry.size(); y++) {
			for (int x = 0; x < txtAry.size(); x++) {
				g.drawString(txtAry.get(y).substring(x), x, y);
			}
		}
		g.dispose();

		// 画像を保存
		//	try {
		File file = new File(filePath);
		//	File file = new File("src\\storage\\out\\" + "output.png");
		//	File file = new File(GetFilePath.getWinDesktop() + "output.png");
		int counter = 1;
		while (file.exists()) {
			filePath = "output" + counter + ext;
			file = new File(filePath);
			counter++;
		}
		file.createNewFile();

		ImageIO.write(image, "png", file);
		//	so.printf("[エクスポート完了] \033[1m\033[34m%s\033[0m\n", filePath);
		//	so.println("[処理終了]");
		//	} catch (Exception e) {
		//	so.println("\033[1m\033[41m[画像書込異常検知] 出力失敗\033[0m\n");
		//	so.println("[処理終了]");
		//	e.printStackTrace();
		//	}
	}        
}
