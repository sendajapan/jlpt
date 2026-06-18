# PathLingo

A JLPT vocabulary learning platform built with Laravel 13 and Alpine.js, featuring a web admin panel and a REST API consumed by an Android mobile client.

## Tech Stack

- **Backend:** PHP 8.4, Laravel 13
- **Frontend:** Alpine.js, Tailwind CSS v4, Vite
- **API:** REST (versioned under `/api/v1/`), documented with Swagger (L5-Swagger)
- **Auth:** Laravel Sanctum (token-based for mobile, session-based for admin)
- **Queue/Cache/Session:** Database driver
- **Testing:** Pest v4

## Requirements

- PHP 8.3+
- Composer
- Node.js 18+ & npm
- MySQL 8+

## Setup

### 1. Clone and install dependencies

```bash
git clone <repository-url>
cd jlpt-laravel-vue
composer install
npm install
```

### 2. Configure environment

```bash
cp .env.example .env
php artisan key:generate
```

Open `.env` and fill in your database credentials:

```
DB_DATABASE=your_database_name
DB_USERNAME=your_database_user
DB_PASSWORD=your_database_password
```

For social login (optional), also set:

```
GOOGLE_CLIENT_IDS=your_google_client_id
FACEBOOK_APP_ID=your_facebook_app_id
FACEBOOK_APP_SECRET=your_facebook_app_secret
```

### 3. Run migrations and seed

```bash
php artisan migrate
php artisan db:seed
```

### 4. Build frontend assets

```bash
npm run build
```

### 5. Start the development server

```bash
composer run dev
```

This starts the Laravel server, queue worker, and Vite dev server concurrently.

The admin panel is available at `http://localhost:8000`.

## API Documentation

Swagger UI is available at `/api/documentation` once the app is running.

Set `L5_SWAGGER_CONST_HOST` in `.env` to the base URL of your server so the docs point to the right host.

## Android Client

The `/android` directory contains the Android project that consumes the `/api/v1/` endpoints. Open it in Android Studio and configure the base URL in the app's network layer to point to your running Laravel instance.

## Running Tests

```bash
php artisan test --compact
```

## Admin Seeder

The `DatabaseSeeder` creates an initial admin user. Check `database/seeders/DatabaseSeeder.php` for the default credentials and change them after first login.

## Storage

Run the following to symlink the public storage disk:

```bash
php artisan storage:link
```
