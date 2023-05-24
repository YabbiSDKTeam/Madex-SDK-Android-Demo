# Madex Android SDK

## Руководство по Интеграции

Версия релиза **1.0.2** | Дата релиза **24.05.2023**

> Минимальные требования:
>
>* Используйте Android API level 19 (Android OS 4.4) и выше.

## Демо приложение
Используйте наше [демо приложение](https://github.com/YabbiSDKTeam/Madex-SDK-Android-Demo) в качестве примера.


## Установка SDK

### Подготовьте Gradle сборки для Android 11
>
>В Android 11 изменился способ запроса приложений и взаимодействия с другими.
приложениями, установленными пользователем на устройстве.
По этой причине убедитесь, что вы используете версию Gradle,
которая соответствует одной из перечисленных [здесь](https://developer.android.com/studio/releases/gradle-plugin#4-0-0).

В зависимости от используемой версии Android Studio вставьте зависимость в файл Gradle.

1. Вставьте следующий код в settings.gradle в корне проекта.  
   **Начиная с Arctic Fox и выше**
    ```gradle
    // Пример project-level settings.gradle
    
    dependencyResolutionManagement {
        repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
        repositories {
            // ... другие репозитории
             maven {
                url "https://mobileadx.ru/maven" // Это репозиторий Madex
            }
            maven {
                url 'https://android-sdk.is.com' // Это репозиторий IronSource
            }
        }
    }
    ```

   **Для версий до Arctic Fox**
    ```gradle
    // Пример project-level settings.gradle
    
    allprojects {
        repositories {
            // ... другие репозитории
            maven {
                url "https://mobileadx.ru/maven" // Это репозиторий Madex
            }
            maven {
                url 'https://android-sdk.is.com' // Это репозиторий IronSource
            }
        }
    }
    ```


2. Вставьте следующий код в app-level build.gradle
   ```gradle
    // Пример app-level build.gradle
   
    android {
        // ... другие опции
        
        defaultConfig {
          // ... другие конфигурации
          
          multiDexEnabled true
        }
    
        compileOptions {
            sourceCompatibility JavaVersion.VERSION_1_8
            targetCompatibility JavaVersion.VERSION_1_8
        }
    }
   ```
3. В этом же файле обновите раздел `dependencies`
   ```gradle
    // Пример app-level build.gradle
    
    dependencies {
        // ... другие зависимости проекта

        implementation 'me.madex.ads:advert:1.0.1' // Это плагин Madex SDK
    }
   ```

Как только gradle config будет сгенерирован, сохраните файл и нажмите **Gradle sync**.

## Настройка проекта

### Настройка Network security config
**Android 9.0 (API 28)** по умолчанию блокирует HTTP трафик. Это может мешать правильному показу рекламы.  
Подробнее вы можете ознакомиться по [ссылке](https://developer.android.com/training/articles/security-config).

Чтобы предотвратить блокировку http-трафика, выполните следующие шаги:

1. Добавьте **Network Security Configuration** файл в **AndroidManifest.xml**:
```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest>
    <application
    ...
    android:networkSecurityConfig="@xml/network_security_config">
    </application>
</manifest>
```
2. Добавьте конфиг, который передает **cleartextTrafficPermitted** true в **network_security_config.xml** файл:
```xml
<?xml version="1.0" encoding="utf-8"?>
<network-security-config>
    <base-config cleartextTrafficPermitted="true">
        <trust-anchors>
            <certificates src="system" />
        </trust-anchors>
    </base-config>
    <domain-config cleartextTrafficPermitted="true">
        <domain includeSubdomains="true">127.0.0.1</domain>
    </domain-config>
</network-security-config>
```

## Инициализация SDK
Импортируйте `Madex`.
```java
import me.madex.ads.Madex;
import me.madex.ads.MadexInterstitialListener;
import me.madex.ads.MadexRewardedListener;
```

### Сбор данных пользователя

#### GDPR и CCPA
GDPR - Это набор правил, призванных дать гражданам ЕС больше контроля над своими личными данными. Любые издатели приложений, которые созданы в ЕС или имеющие пользователей, базирующихся в Европе, обязаны соблюдать GDPR или рискуют столкнуться с большими штрафами

Для того чтобы SDK и наши поставщики рекламы могли предоставлять рекламу, которая наиболее релевантна для ваших пользователей, как издателю мобильных приложений, вам необходимо получить явное согласие пользователей в регионах, попадающих под действие законов GDPR и CCPA.

#### Установка разрешения на сбор данных
Если пользователь дал согласие на сбор данных, установите `setUserConsent` в `true`
```java
Madex.setUserConsent(true);
```

### Установка возрастного ограничение
Посредническая платформа ironSource позволяет издателям передавать настройки возрастных ограничений.

* Используйте следующий код, чтобы установить возрастное ограничение для `AppLovin` с параметром
  `true`:
    ```java
    Madex.setMetaData("AppLovin_AgeRestrictedUser","true");
    ```

* Используйте следующий код, чтобы установить возрастное ограничение для `Digital Turbine` с параметром `true`:
    ```java
    Madex.setMetaData("DT_IsChild","true");
    ```

* Используйте следующий код, чтобы установить возрастное ограничение для `InMobi` с параметром `true`:
    ```java
    Madex.setMetaData("InMobi_AgeRestricted","true");
    ```
* Используйте следующий код, чтобы установить возрастное ограничение для `UnityAds`с параметром `true`:
    ```java
    Madex.setMetaData("UnityAds_coppa","true");
    ```

### Инициализация
Теперь SDK готова к инициализации. Используйте код ниже, чтобы SDK заработал в вашем проекте.
```java
Madex.initialize(app_id);
```

* `app_id` - идентификатор приложения.


Замените `app_id` на идентификатор приложения из [личного кабинета](https://platform.ironsrc.com/partners/dashboard).

Ниже представлен полный код.

Мы рекомендуем вызывать инициализацию SDK в вашей `MainActivity` - в `onCreate` методе.

```java
import me.madex.ads.Madex;
import me.madex.ads.MadexInterstitialListener;
import me.madex.ads.MadexRewardedListener;

@Override
protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Madex.setUserConsent(true);
        Madex.initialize("замените_на_свой_id");
        }
```

## Режим отладки
В режиме отладки SDK логирует ошибки и события. По умолчанию выключен.

Для включения режима отладки используйте метод `enableDebug`.

```java
Madex.enableDebug(true);
```

## Типы рекламы

Вы можете подключить 2 типа рекламы в свое приложение.

* Полноэкранная реклама - баннер на весь экран, который можно закрыть через несколько секунд.
* Полноэкранная реклама с вознаграждением - видео, после просмотра которого пользователю можно выдать награду.

Ознакомьтесь с детальной документацией по каждому типу рекламы

1. [Полноэкранная реклама](INTERSTITIAL.md)
2. [Полноэкранная реклама с вознаграждением](REWARDED.md)

## Подготовьте ваше Android приложение к публикации

В соответствии с [политикой Google](https://support.google.com/googleplay/android-developer/answer/9857753?hl=ru), разрешения на определения местоположения могут запрашиваться только для функций, имеющих отношение к основному функционалу приложения. Вы не можете запрашивать доступ к данным о местоположении исключительно с целью предоставления рекламы или аналитики.



**Если вы не используете местоположения как одну из основных функций вашего приложения:**
* Добавьте следующий код в `AndroidManifest.xml` вашего приложения:
```xml
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" tools:node="remove" />
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" tools:node="remove" />
```
* Обновите приложение в Google Play. В процессе публикации убедитесь, что в Google Play Console нет предупреждений о наличии разрешения местоположения.

**Если ваше приложение использует местоположение, как одну из основных функций:**
* Заполните форму декларации разрешений на местоположение в [Google Play Console](https://play.google.com/console/u/0/developers/app/app-content/permission-declarations). Подробнее о форме декларации вы можете прочитать [здесь](https://support.google.com/googleplay/android-developer/answer/9799150?hl=en#zippy=%2Cwhere-do-i-find-the-declaration).
* Обновите приложение в Google Play. В процессе публикации убедитесь, что в Google Play Console нет предупреждений о наличии разрешения местоположения.