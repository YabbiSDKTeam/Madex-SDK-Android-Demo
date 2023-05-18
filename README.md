# Madex Android SDK

## Руководство по Интеграции

Версия релиза **1.0.0** | Дата релиза **18.05.2023**

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

Если вы используете адаптеры для сторонних рекламных сетей,так же добавьте их зависимости.

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
            maven {
                url  "https://dl-maven-android.mintegral.com/repository/mbridge_android_sdk_oversea"  // Это репозиторий для Mintegral
            }
        }
    }
    ```

   **Для версий до Arctic Fox**
    ```gradle
    // Пример project-level settings.gradle
    
    allprojects {
        repositories {
            // ... other project repositories
            maven {
                url "https://mobileadx.ru/maven" // Это репозиторий Madex
            }
            maven {
                url 'https://android-sdk.is.com' // Это репозиторий IronSource
            }
            maven {
                url  "https://dl-maven-android.mintegral.com/repository/mbridge_android_sdk_oversea"  // Это репозиторий для Mintegral
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
   * Для подключения плагина со всеми рекламными сетями вставьте следующий код.
   ```gradle
    // Пример app-level build.gradle
    
    dependencies {
        // ... другие зависимости проекта

        implementation 'me.madex.ads:sdk:2.4.1' // Это плагин Madex SDK
    }
   ```
   * Вы можете подключить рекламные сети выборочно. Для этого вставьте следующий код.
   ```gradle
    // Пример app-level build.gradle
    
    dependencies {
        // ... другие зависимости проекта

        implementation 'me.madex.ads:core:1.0.0' // Это обязательная зависимость SDK
        implementation 'me.madex.ads.adapters:yandex:1.0.0' // Это рекламная сеть Yandex
        implementation 'me.madex.ads.adapters:ironsource:1.0.0' // Это рекламная сеть IronSource
        implementation 'me.madex.ads.adapters:mintegral:1.0.0' // Это рекламная сеть Mintegral
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
import me.madex.ads.MadexConfiguration;
import me.madex.ads.MadexInterstitialListener;
import me.madex.ads.MadexRewardedListener;
import me.madex.ads.mediation.MadexAdapterCustomKeys;
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

### Работа сторонних рекламных сетей
Для работы сторонних рекламных сетей необходимо добавить идентификаторы для каждой рекламной сети.
```java
// Установите для показа полноэкранной рекламы Яндекса
Madex.setCustomParams(MadexAdapterCustomKeys.yandexInterstitialID, "замените_на_свой_id");

// Установите для показа рекламы с вознаграждением Яндекса
Madex.setCustomParams(MadexAdapterCustomKeys.yandexInterstitialID, "замените_на_свой_id");

// Установите для показа рекламы от IronSource
Madex.setCustomParams(MadexAdapterCustomKeys.ironSourceAppID, "замените_на_свой_id");

// Установите для показа полноэкранной рекламы IronSource
Madex.setCustomParams(MadexAdapterCustomKeys.ironSourceInterstitialPlacementID, "замените_на_свой_id");

// Установите для показа рекламы с вознаграждением IronSource
Madex.setCustomParams(MadexAdapterCustomKeys.ironSourceRewardedPlacementID, "замените_на_свой_id");

// Установите для показа рекламы от Mintegral
Madex.setCustomParams(MadexAdapterCustomKeys.mintegralAppID, "замените_на_свой_id");
Madex.setCustomParams(MadexAdapterCustomKeys.mintegralApiKey, "замените_на_свой_id");

// Установите для показа полноэкранной рекламы Mintegral
Madex.setCustomParams(MadexAdapterCustomKeys.mintegralInterstitialPlacementId, "замените_на_свой_id");
Madex.setCustomParams(MadexAdapterCustomKeys.mintegralInterstitialUnitId, "замените_на_свой_id");

// Установите для показа рекламы с вознаграждением Mintegral
Madex.setCustomParams(MadexAdapterCustomKeys.mintegralRewardedPlacementId, "замените_на_свой_id");
Madex.setCustomParams(MadexAdapterCustomKeys.mintegralRewardedUnitId, "замените_на_свой_id");
```
> Используйте метод `setCustomParams` до вызова метода `initialize`.

### Инициализация
Теперь SDK готова к инициализации. Используйте код ниже, чтобы SDK заработал в вашем проекте.
```java
final MadexConfiguration config = new MadexConfiguration(
    "publisher_id",
    "interstitial_id",
    "rewarded_id"
);

Madex.initialize(config);
```

* `publisher_id` - идентификатор издателя. Обязателен для заполнения.
* `interstitial_id` - идентификатор полноэкранной рекламы. Может оставаться пустой строкой.
* `rewarded_id` - идентификатор полноэкранной рекламы с вознаграждением. Может оставаться пустой строкой.


1. Замените `publisher_id` на идентификатор издателя из [личного кабинета](https://mobileadx.ru/settings).
2. Замените `interstitial_id` на ключ соответствующий баннерной рекламе из [личного кабинета](https://mobileadx.ru).
3. Замените `rewarded_id` на ключ соответствующий видео с вознаграждением из [личного кабинета](https://mobileadx.ru).

Ниже представлен полный код.

Мы рекомендуем вызывать инициализацию SDK в вашей `MainActivity` - в `onCreate` методе.

```java
import me.madex.ads.Madex;
import me.madex.ads.MadexConfiguration;
import me.madex.ads.MadexInterstitialListener;
import me.madex.ads.MadexRewardedListener;
import me.madex.ads.mediation.MadexAdapterCustomKeys;

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    Madex.setCustomParams(MadexAdapterCustomKeys.yandexInterstitialID, "замените_на_свой_id");

    Madex.setCustomParams(MadexAdapterCustomKeys.yandexInterstitialID, "замените_на_свой_id");

    Madex.setCustomParams(MadexAdapterCustomKeys.ironSourceAppID, "замените_на_свой_id");

    Madex.setCustomParams(MadexAdapterCustomKeys.ironSourceInterstitialPlacementID, "замените_на_свой_id");

    Madex.setCustomParams(MadexAdapterCustomKeys.ironSourceRewardedPlacementID, "замените_на_свой_id");

    Madex.setCustomParams(MadexAdapterCustomKeys.mintegralAppID, "замените_на_свой_id");
    Madex.setCustomParams(MadexAdapterCustomKeys.mintegralApiKey, "замените_на_свой_id");

    Madex.setCustomParams(MadexAdapterCustomKeys.mintegralInterstitialPlacementId, "замените_на_свой_id");
    Madex.setCustomParams(MadexAdapterCustomKeys.mintegralInterstitialUnitId, "замените_на_свой_id");

    Madex.setCustomParams(MadexAdapterCustomKeys.mintegralRewardedPlacementId, "замените_на_свой_id");
    Madex.setCustomParams(MadexAdapterCustomKeys.mintegralRewardedUnitId, "замените_на_свой_id");

    final MadexConfiguration config = new MadexConfiguration(
        "publisher_id",
        "interstitial_id",
        "rewarded_id"
    );
    
    Madex.setUserConsent(true);
    
    Madex.initialize(config);
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
