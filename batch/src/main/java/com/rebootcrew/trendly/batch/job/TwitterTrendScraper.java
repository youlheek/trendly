package com.rebootcrew.trendly.batch.job;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class TwitterTrendScraper {
	// 🔹 트위터 로그인 정보 (직접 입력 or 환경 변수 사용)
	private static final String TWITTER_USERNAME = "";
	private static final String TWITTER_PASSWORD = "";

	public static void main(String[] args) {
		// ✅ 최신 ChromeDriver 사용
		WebDriverManager.chromedriver().setup();
		System.setProperty("webdriver.chrome.driver", "C:\\chromedriver.exe");

		// ✅ Chrome 브라우저 설정 (자동화 감지 우회)
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--headless"); // 필요 시 활성화
		options.addArguments("--disable-gpu");
		options.addArguments("--window-size=1920,1080");
		options.addArguments("--disable-blink-features=AutomationControlled"); // 자동화 감지 우회
		options.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/133.0.0.0 Safari/537.36"); // 최신 User-Agent 설정

		// ✅ WebDriver 실행
		WebDriver driver = new ChromeDriver(options);
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));  // 🔹 암시적 대기 추가
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20)); // 🔹 명시적 대기 증가

		try {
			// ✅ 트위터 로그인 페이지 접속
			driver.get("https://x.com/i/flow/login");
			System.out.println("🔹 트위터 로그인 페이지 접속 완료");

			// ✅ 사용자명 입력 (StaleElementReferenceException 예외 방지)
			WebElement usernameInput = wait.until(ExpectedConditions.presenceOfElementLocated(
					By.xpath("//input[@autocomplete='username']")
			));

			boolean isStale = true;
			int attempts = 0;
			while (isStale && attempts < 3) { // 최대 3번 재시도
				try {
					usernameInput.sendKeys(TWITTER_USERNAME);
					usernameInput.sendKeys(Keys.ENTER); // ✅ "Enter" 키로 로그인 진행
					isStale = false; // 성공하면 루프 종료
				} catch (StaleElementReferenceException e) {
					System.out.println("🔹 StaleElementReferenceException 발생, 다시 시도 중...");
					usernameInput = wait.until(ExpectedConditions.presenceOfElementLocated(
							By.xpath("//input[@autocomplete='username']")
					));
				}
				attempts++;
			}
			System.out.println("🔹 사용자명 입력 완료");

			// ✅ 비밀번호 입력
			WebElement passwordInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
					By.xpath("//input[@type='password']")
			));
			passwordInput.sendKeys(TWITTER_PASSWORD);
			passwordInput.sendKeys(Keys.ENTER); // ✅ "Enter" 키로 로그인 진행
			System.out.println("🔹 비밀번호 입력 완료");

			// ✅ 로그인 후 트렌드 페이지 이동
			wait.until(ExpectedConditions.urlContains("home")); // 홈 화면 로딩 확인
			driver.get("https://x.com/explore/tabs/trending");
			System.out.println("🔹 트렌드 페이지 이동 완료");

			// ✅ 트렌드 10개가 로딩될 때까지 스크롤 (최대 5회)
			int scrollAttempts = 0;
			while (scrollAttempts < 5) { // 🔹 최대 5번 스크롤
				((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 700);");
				Thread.sleep(2000); // 스크롤 후 대기
				scrollAttempts++;
			}

			// ✅ 최신 트렌드 데이터 가져오기 (새로운 XPath 사용)
			List<WebElement> trends = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
					By.xpath("//div[@data-testid='trend']/div/div[2]//span[contains(@class, 'r-qvutc0')]")
			));

			System.out.println("🔹 트위터 실시간 트렌드 🔹");
			int count = 1;
			for (WebElement trend : trends) {
				if (!trend.getText().isEmpty()) {
					System.out.println(count + ". " + trend.getText());
					count++;
					if (count > 20) break; // 상위 10개 트렌드만 출력
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			driver.quit();
		}
	}
}
