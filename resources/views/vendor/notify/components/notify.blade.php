@if (session()->has('notify'))
    @php
        $notificationModel = session()->get('notify.model');
        if (is_string($notificationModel)) {
            $notificationModel = \Mckenziearts\Notify\Enums\NotificationModel::from($notificationModel);
        }
    @endphp

    @if ($notificationModel)
        @include($notificationModel->getViewName())
    @endif

    <script>
        const notification = document.querySelector('div.notify');
        const notify = {
            timeout: {{ session()->get('notify.duration', config('notify.timeout', 5000)) }},
        }

        if (notification) {
            setTimeout(() => {
                notification.remove();
            }, notify.timeout);
        }
    </script>
@endif
