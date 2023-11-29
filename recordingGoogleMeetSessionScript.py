# В начале 'pip install selenium'


from selenium import webdriver
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
import time
from selenium.webdriver.firefox.options import Options

def login_google(account_email, account_password):
    # Путь к драйверу браузера, например, для Chrome
    driver_path = 'chrome.exe'
    
    # Опции для Chrome
    chrome_options = webdriver.ChromeOptions()
    
    # Добавляем опцию для работы в режиме автоматизации
    chrome_options.add_argument('--headless')  # Убрать эту строку, если хотите видеть браузер
    
    # Создаем экземпляр веб-драйвера
    driver = webdriver.Chrome()
    
    # Открываем страницу входа Google
    driver.get('https://accounts.google.com/')
    
    # Находим поле ввода email
    email_input = driver.find_element(By.NAME, 'identifier')
    email_input.send_keys(account_email)
    email_input.send_keys(Keys.RETURN)
    
    # Ждем несколько секунд, чтобы загрузилась страница ввода пароля
    time.sleep(10)
    
    # Находим поле ввода пароля по другому атрибуту
    #password_input = driver.find_element(By.NAME, 'password')
    password_input = driver.find_element(By.CSS_SELECTOR, 'input[type="password"]')
    password_input.send_keys(account_password)
    password_input.send_keys(Keys.RETURN)
    
    # Ждем несколько секунд, чтобы авторизация завершилась
    time.sleep(10)
    
    return driver


def join_and_record_meeting(driver, meeting_link):
    # Открываем Google Meet
    driver.get(meeting_link)
    
    # Ждем несколько секунд, чтобы браузер полностью загрузил страницу
    time.sleep(10)
    
    #micro_button = driver.find_element(By.CSS_SELECTOR, 'div[jsname="rZHESd"]')
    #micro_button.click()
    #time.sleep(10)

    # Находим кнопку "Присоединиться"
    join_button = driver.find_element(By.CSS_SELECTOR, 'div[jsname="Qx7uuf"]')
    join_button.click()
    
    # Ждем несколько секунд для присоединения к сессии
    time.sleep(20)
    
    # Нажимаем на кнопку записи
    record_button = driver.find_element(By.CSS_SELECTOR, 'div[jsname="NPEF7B"]')
    record_button.click()
    
    # Ждем несколько секунд, чтобы запись началась
    time.sleep(5)



# Пример использования:
account_email = 'login'
account_password = 'pswd'
meeting_link = 'link'

driver = login_google(account_email, account_password)
join_and_record_meeting(driver, meeting_link)

# После завершения работы сессии закрываем браузер
driver.quit()
